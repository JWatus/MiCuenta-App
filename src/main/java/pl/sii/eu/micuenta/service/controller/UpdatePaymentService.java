package pl.sii.eu.micuenta.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pl.sii.eu.micuenta.model.Debt;
import pl.sii.eu.micuenta.model.Debtor;
import pl.sii.eu.micuenta.model.Payment;
import pl.sii.eu.micuenta.model.form.PaymentConfirmation;
import pl.sii.eu.micuenta.model.form.PaymentDeclaration;
import pl.sii.eu.micuenta.repository.AccountsRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UpdatePaymentService {

    private ValidationService validationService;
    private AccountsRepository accountsRepository;
    private DebtCalculatorService debtCalculatorService;

    public UpdatePaymentService(AccountsRepository accountsRepository,
                                DebtCalculatorService debtCalculatorService,
                                ValidationService validationService) {
        this.debtCalculatorService = debtCalculatorService;
        this.accountsRepository = accountsRepository;
        this.validationService = validationService;
    }

    private static final Logger logger = LoggerFactory.getLogger(UpdatePaymentService.class);

    public ResponseEntity updateDebtsPaymentsBasedOnPaymentConfirmation(@RequestBody PaymentConfirmation paymentConfirmation) {

        PaymentDeclaration paymentDeclaration = paymentConfirmation.getPaymentDeclaration();

        BigDecimal paymentAmount = paymentDeclaration.getPaymentAmount().setScale(2, RoundingMode.HALF_EVEN);
        String ssn = paymentDeclaration.getSsn();
        String debtUuid = paymentDeclaration.getDebtUuid();

        if (validationService.notValidPaymentAmount(paymentAmount, ssn)) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Debtor debtor = accountsRepository.findFirstBySsn(ssn);

        if (debtUuid.isEmpty()) {
            handlingEmptyDebtId(debtor, paymentConfirmation);
            return new ResponseEntity<String>(HttpStatus.OK);
        }

        for (Debt chosenDebt : debtor.getSetOfDebts()) {
            if (chosenDebt.getUuid().equals(debtUuid)) {
                handlingChosenDebtId(chosenDebt, debtor, paymentConfirmation);
                return new ResponseEntity<String>(HttpStatus.OK);
            }
        }
        return responseEntity;
    }

    private void handlingEmptyDebtId(Debtor debtor, PaymentConfirmation paymentConfirmation) {

        BigDecimal paymentAmount = paymentConfirmation.getPaymentDeclaration().getPaymentAmount();

        BigDecimal sumOfDebts = debtCalculatorService.getSumOfDebts(debtor);

        List<Debt> oldestDebts = debtCalculatorService.getListOfOldestDebts(debtor);

        boolean paymentIsNotBiggerThanSumOfDebts = paymentAmount.compareTo(sumOfDebts) <= 0;
        boolean paymentIsBiggerThanSumOfDebts = paymentAmount.compareTo(sumOfDebts) > 0;

        if (!oldestDebts.isEmpty() && paymentIsNotBiggerThanSumOfDebts) {
            payOldestDebts(paymentAmount, oldestDebts, paymentConfirmation);
        } else if (!oldestDebts.isEmpty() && paymentIsBiggerThanSumOfDebts) {
            payAllDebts(debtor, sumOfDebts, paymentConfirmation);
        }
    }

    private void handlingChosenDebtId(Debt chosenDebt, Debtor debtor, PaymentConfirmation paymentConfirmation) {

        BigDecimal paymentAmount = paymentConfirmation.getPaymentDeclaration().getPaymentAmount();

        BigDecimal sumOfDebts = debtCalculatorService.getSumOfDebts(debtor);
        List<Debt> oldestDebts = debtCalculatorService.getListOfOldestDebts(debtor);

        boolean paymentIsNotBiggerThanSumOfDebts = paymentAmount.compareTo(sumOfDebts) <= 0;
        boolean paymentIsBiggerThanSumOfDebts = paymentAmount.compareTo(sumOfDebts) > 0;

        if (!oldestDebts.isEmpty() && paymentIsNotBiggerThanSumOfDebts) {
            createPaymentPlanDependingOnAmount(chosenDebt, paymentAmount, oldestDebts, paymentConfirmation);
        } else if (!oldestDebts.isEmpty() && paymentIsBiggerThanSumOfDebts) {
            payAllDebts(debtor, sumOfDebts, paymentConfirmation);
        }
    }

    private void createPaymentPlanDependingOnAmount(
            Debt chosenDebt,
            BigDecimal paymentAmount,
            List<Debt> oldestDebts,
            PaymentConfirmation paymentConfirmation) {

        BigDecimal debtLeftToPaid = debtCalculatorService.getDebtLeftToPaid(chosenDebt);

        logger.info("Payments list for debt {} has been actualized.", chosenDebt.getUuid());

        if (paymentAmount.compareTo(debtLeftToPaid) <= 0) {
            addPaymentToDebtsSetOfPayments(paymentAmount, chosenDebt, paymentConfirmation);
        } else {
            payChosenDebtAndOthersByDate(paymentAmount, oldestDebts, chosenDebt, paymentConfirmation);
        }
    }

    private void payChosenDebtAndOthersByDate(BigDecimal paymentAmount, List<Debt> oldestDebts,
                                              Debt chosenDebt, PaymentConfirmation paymentConfirmation) {

        if (debtCalculatorService.getSumOfPayments(chosenDebt).compareTo(chosenDebt.getDebtAmount()) < 0) {
            BigDecimal restAmount = chosenDebt.getDebtAmount().subtract(debtCalculatorService.getSumOfPayments(chosenDebt));
            addPaymentToDebtsSetOfPayments(restAmount, chosenDebt, paymentConfirmation);
            paymentAmount = paymentAmount.subtract(restAmount);
        }

        List<Debt> debtsWithoutChosen = new ArrayList<>();
        for (Debt d : oldestDebts) {
            if (!d.getUuid().equals(chosenDebt.getUuid())) {
                debtsWithoutChosen.add(d);
            }
        }
        payOldestDebts(paymentAmount, debtsWithoutChosen, paymentConfirmation);
    }

    private void payOldestDebts(BigDecimal paymentAmount, List<Debt> oldestDebts,
                                PaymentConfirmation paymentConfirmation) {

        for (int i = 0; i < oldestDebts.size(); i++) {
            Debt oldestDebt = oldestDebts.get(i);
            BigDecimal debtLeftToPaid = debtCalculatorService.getDebtLeftToPaid(oldestDebt);

            logger.info("Payments list for debt {} has been actualized.", oldestDebt.getUuid());

            if (paymentAmount.compareTo(debtLeftToPaid) <= 0) {
                addPaymentToDebtsSetOfPayments(paymentAmount, oldestDebt, paymentConfirmation);
                break;
            } else {
                paymentAmount = addPaymentToDebtsSetOfPaymentsAndGetRemainingPaymentAmount(
                        paymentAmount, oldestDebt, debtLeftToPaid, paymentConfirmation);
            }
        }
    }

    private void payAllDebts(Debtor debtor, BigDecimal sumOfDebts, PaymentConfirmation paymentConfirmation) {

        logger.info("All debts has been paid. Surplus: {}.",
                paymentConfirmation.getPaymentDeclaration().getPaymentAmount().subtract(sumOfDebts));

        List<Debt> listOfDebts = new ArrayList<>();
        debtor.getSetOfDebts().forEach(d -> listOfDebts.add(d));

        for (int i = 0; i < listOfDebts.size(); i++) {
            Debt debt = listOfDebts.get(i);
            addNewPaymentToDebtsPayments(debt, new Payment(
                    LocalDate.now(),
                    debt.getDebtAmount().subtract(debtCalculatorService.getSumOfPayments(debt)),
                    paymentConfirmation.getCreditCard(),
                    paymentConfirmation.getClientId()));
        }
    }

    private void addPaymentToDebtsSetOfPayments(BigDecimal paymentAmount, Debt debt,
                                                PaymentConfirmation paymentConfirmation) {

        Payment payment = new Payment(LocalDate.now(), paymentAmount,
                paymentConfirmation.getCreditCard(), paymentConfirmation.getClientId());

        addNewPaymentToDebtsPayments(debt, payment);
    }

    private BigDecimal addPaymentToDebtsSetOfPaymentsAndGetRemainingPaymentAmount(
            BigDecimal paymentAmount, Debt debt, BigDecimal debtLeftToPaid, PaymentConfirmation paymentConfirmation) {

        Payment payment = new Payment(LocalDate.now(), debtLeftToPaid,
                paymentConfirmation.getCreditCard(), paymentConfirmation.getClientId());

        addNewPaymentToDebtsPayments(debt, payment);

        paymentAmount = paymentAmount.subtract(debtLeftToPaid);
        return paymentAmount;
    }

    private void addNewPaymentToDebtsPayments(Debt debt, Payment payment) {
        if (debtCalculatorService.getSumOfPayments(debt).compareTo(debt.getDebtAmount()) < 0) {
            debt.addToSetOfPayments(payment);
            accountsRepository.save(debt.getDebtor());
        }
    }
}








