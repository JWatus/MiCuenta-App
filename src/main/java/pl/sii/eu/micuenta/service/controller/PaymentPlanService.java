package pl.sii.eu.micuenta.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pl.sii.eu.micuenta.model.Debt;
import pl.sii.eu.micuenta.model.Debtor;
import pl.sii.eu.micuenta.model.form.PaymentDeclaration;
import pl.sii.eu.micuenta.model.form.PaymentPlan;
import pl.sii.eu.micuenta.model.form.PlannedPayment;
import pl.sii.eu.micuenta.repository.AccountsRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

@Service
public class PaymentPlanService {

    private final DebtCalculatorService debtCalculatorService;
    private AccountsRepository accountsRepository;

    public PaymentPlanService(AccountsRepository accountsRepository, DebtCalculatorService debtCalculatorService) {
        this.accountsRepository = accountsRepository;
        this.debtCalculatorService = debtCalculatorService;
    }

    private static final Logger logger = LoggerFactory.getLogger(PaymentPlanService.class);

    public PaymentPlan getPaymentPlanBasedOnPaymentDeclaration(@RequestBody PaymentDeclaration paymentDeclaration) {
        BigDecimal paymentAmount = paymentDeclaration.getPaymentAmount().setScale(2, RoundingMode.HALF_EVEN);
        String ssn = paymentDeclaration.getSsn();
        String debtUuid = paymentDeclaration.getDebtUuid();

        if (notValidPaymentAmount(paymentAmount, ssn)) {
            return new PaymentPlan("Payment amount is not valid.", ssn, emptyList());
        }

        PaymentPlan paymentPlan = new PaymentPlan("There is no debt with uuid " + debtUuid, ssn, emptyList());
        Debtor debtor = accountsRepository.findFirstBySsn(ssn);

        if (debtUuid.isEmpty()) {
            return handlingEmptyDebtId(debtor, paymentAmount);
        }

        for (Debt chosenDebt : debtor.getSetOfDebts()) {
            if (chosenDebt.getUuid().equals(debtUuid)) {
                return handlingChosenDebtId(chosenDebt, paymentAmount, debtor);
            }
        }

        return paymentPlan;
    }

    private boolean notValidPaymentAmount(BigDecimal paymentAmount, String ssn) {
        if (paymentAmount.compareTo(BigDecimal.ZERO) > 0) {
            logger.info("Received payment: {} for user with ssn {}.",
                    paymentAmount.setScale(2, RoundingMode.HALF_EVEN), ssn);
            return false;
        } else {
            logger.info("Not valid payment amount.");
            return true;
        }
    }

    private PaymentPlan handlingEmptyDebtId(Debtor debtor, BigDecimal paymentAmount) {

        List<PlannedPayment> plannedPaymentList = new ArrayList<>();
        String ssn = debtor.getSsn();
        String message = "Your payment amount is " + paymentAmount;
        PaymentPlan paymentPlan = new PaymentPlan(message, ssn, plannedPaymentList);

        BigDecimal sumOfDebts = debtCalculatorService.getSumOfDebts(debtor);

        List<Debt> oldestDebts = debtCalculatorService.getListOfOldestDebts(debtor);

        boolean paymentIsNotBiggerThanSumOfDebts = paymentAmount.compareTo(sumOfDebts) <= 0;
        boolean paymentIsBiggerThanSumOfDebts = paymentAmount.compareTo(sumOfDebts) > 0;

        if (!oldestDebts.isEmpty() && paymentIsNotBiggerThanSumOfDebts) {
            return payOldestDebts(paymentAmount, paymentPlan, oldestDebts);
        } else if (!oldestDebts.isEmpty() && paymentIsBiggerThanSumOfDebts) {
            return payAllDebts(debtor, paymentAmount, plannedPaymentList, sumOfDebts);
        } else {
            return new PaymentPlan("You don't have any debts to paid.", ssn, emptyList());
        }
    }

    private PaymentPlan handlingChosenDebtId(Debt chosenDebt, BigDecimal paymentAmount, Debtor debtor) {

        List<PlannedPayment> plannedPaymentList = new ArrayList<>();
        String ssn = debtor.getSsn();
        String message = "Your payment amount is " + paymentAmount;
        PaymentPlan paymentPlan = new PaymentPlan(message, ssn, plannedPaymentList);

        BigDecimal sumOfDebts = debtCalculatorService.getSumOfDebts(debtor);
        List<Debt> oldestDebts = debtCalculatorService.getListOfOldestDebts(debtor);

        boolean paymentIsNotBiggerThanSumOfDebts = paymentAmount.compareTo(sumOfDebts) <= 0;
        boolean paymentIsBiggerThanSumOfDebts = paymentAmount.compareTo(sumOfDebts) > 0;

        if (!oldestDebts.isEmpty() && paymentIsNotBiggerThanSumOfDebts) {
            return createPaymentPlanDependingOnAmount(chosenDebt, paymentAmount, paymentPlan, oldestDebts);
        } else if (!oldestDebts.isEmpty() && paymentIsBiggerThanSumOfDebts) {
            return payAllDebts(debtor, paymentAmount, plannedPaymentList, sumOfDebts);
        } else {
            return new PaymentPlan("You don't have any debts to paid.", ssn, emptyList());
        }
    }

    private PaymentPlan createPaymentPlanDependingOnAmount(Debt chosenDebt, BigDecimal paymentAmount, PaymentPlan paymentPlan, List<Debt> oldestDebts) {
        BigDecimal debtLeftToPaid = debtCalculatorService.getDebtLeftToPaid(chosenDebt);

        logger.info("Payments list for debt {} is ready to be actualized.", chosenDebt.getUuid());

        if (paymentAmount.compareTo(debtLeftToPaid) <= 0) {
            addPaymentToPlan(paymentAmount, paymentPlan, chosenDebt);
            return paymentPlan;
        } else {
            return payChosenDebtAndOthersByDate(paymentAmount, paymentPlan, oldestDebts, chosenDebt);
        }
    }

    private PaymentPlan payChosenDebtAndOthersByDate(BigDecimal paymentAmount, PaymentPlan paymentPlan, List<Debt> oldestDebts, Debt chosenDebt) {

        addPaymentToPlan(chosenDebt.getDebtAmount().subtract(debtCalculatorService.getSumOfPayments(chosenDebt)), paymentPlan, chosenDebt);
        paymentAmount = paymentAmount.subtract(chosenDebt.getDebtAmount());

        List<Debt> debtsWithoutChosen = new ArrayList<>();

        for (Debt d : oldestDebts) {
            if (!d.getUuid().equals(chosenDebt.getUuid())) {
                debtsWithoutChosen.add(d);
            }
        }
        payOldestDebts(paymentAmount, paymentPlan, debtsWithoutChosen);
        return paymentPlan;
    }

    private PaymentPlan payOldestDebts(BigDecimal paymentAmount, PaymentPlan paymentPlan, List<Debt> oldestDebts) {

        logger.info("PaymentPlan for {} has been actualized.", paymentPlan.getSsn());

        for (int i = 0; i < oldestDebts.size(); i++) {
            Debt oldestDebt = oldestDebts.get(i);
            BigDecimal debtLeftToPaid = debtCalculatorService.getDebtLeftToPaid(oldestDebt);

            logger.info("Payments list for debt {} is ready to be actualized.", oldestDebt.getUuid());

            if (paymentAmount.compareTo(debtLeftToPaid) <= 0) {
                addPaymentToPlan(paymentAmount, paymentPlan, oldestDebt);
                break;
            } else {
                paymentAmount = addPaymentToPlanAndGetRemainingPaymentAmount(paymentAmount, paymentPlan, oldestDebt, debtLeftToPaid);
            }
        }
        return paymentPlan;
    }

    private PaymentPlan payAllDebts(Debtor debtor, BigDecimal paymentAmount, List<PlannedPayment> plannedPaymentList, BigDecimal sumOfDebts) {

        logger.info("All debts can been paid. Surplus: {}.", paymentAmount.subtract(sumOfDebts));

        PaymentPlan paymentPlan = new PaymentPlan
                ("All debts will be paid. You have " + paymentAmount.subtract(sumOfDebts) + " of surplus.", debtor.getSsn(), plannedPaymentList);
        for (Debt d : debtor.getSetOfDebts()) {
            BigDecimal sumOfPayments = debtCalculatorService.getSumOfPayments(d);
            plannedPaymentList.add(new PlannedPayment(d.getUuid(), d.getDebtAmount().subtract(sumOfPayments)));
        }
        return paymentPlan;
    }

    private void addPaymentToPlan(BigDecimal paymentAmount, PaymentPlan paymentPlan, Debt debt) {
        List<PlannedPayment> plannedPaymentList = paymentPlan.getPlannedPaymentList();
        plannedPaymentList.add(new PlannedPayment(debt.getUuid(), paymentAmount));
        paymentPlan.setPlannedPaymentList(plannedPaymentList);
    }

    private BigDecimal addPaymentToPlanAndGetRemainingPaymentAmount(BigDecimal paymentAmount, PaymentPlan paymentPlan, Debt debt, BigDecimal debtLeftToPaid) {
        paymentPlan
                .getPlannedPaymentList()
                .add(new PlannedPayment(debt.getUuid(), debtLeftToPaid));
        paymentAmount = paymentAmount.subtract(debtLeftToPaid);
        return paymentAmount;
    }
}