package pl.sii.eu.micuenta.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pl.sii.eu.micuenta.model.model_dto.form.PaymentConfirmation;
import pl.sii.eu.micuenta.model.model_dto.form.PaymentDeclaration;
import pl.sii.eu.micuenta.model.model_entity.CreditCardEntity;
import pl.sii.eu.micuenta.model.model_entity.DebtEntity;
import pl.sii.eu.micuenta.model.model_entity.DebtorEntity;
import pl.sii.eu.micuenta.model.model_entity.PaymentEntity;
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
        DebtorEntity debtorEntity = accountsRepository.findFirstBySsn(ssn);

        if (debtUuid.isEmpty()) {
            handlingEmptyDebtId(debtorEntity, paymentConfirmation);
            return new ResponseEntity<String>(HttpStatus.OK);
        }

        for (DebtEntity chosenDebtEntity : debtorEntity.getDebtEntities()) {
            if (chosenDebtEntity.getUuid().equals(debtUuid)) {
                handlingChosenDebtId(chosenDebtEntity, debtorEntity, paymentConfirmation);
                return new ResponseEntity<String>(HttpStatus.OK);
            }
        }
        return responseEntity;
    }

    private void handlingEmptyDebtId(DebtorEntity debtorEntity, PaymentConfirmation paymentConfirmation) {

        BigDecimal paymentAmount = paymentConfirmation.getPaymentDeclaration().getPaymentAmount();

        BigDecimal sumOfDebts = debtCalculatorService.getSumOfDebts(debtorEntity);

        List<DebtEntity> oldestDebtEntities = debtCalculatorService.getListOfOldestDebts(debtorEntity);

        boolean paymentIsNotBiggerThanSumOfDebts = paymentAmount.compareTo(sumOfDebts) <= 0;
        boolean paymentIsBiggerThanSumOfDebts = paymentAmount.compareTo(sumOfDebts) > 0;

        if (!oldestDebtEntities.isEmpty() && paymentIsNotBiggerThanSumOfDebts) {
            payOldestDebts(paymentAmount, oldestDebtEntities, paymentConfirmation);
        } else if (!oldestDebtEntities.isEmpty() && paymentIsBiggerThanSumOfDebts) {
            payAllDebts(debtorEntity, sumOfDebts, paymentConfirmation);
        }
    }

    private void handlingChosenDebtId(DebtEntity chosenDebtEntity, DebtorEntity debtorEntity, PaymentConfirmation paymentConfirmation) {

        BigDecimal paymentAmount = paymentConfirmation.getPaymentDeclaration().getPaymentAmount();

        BigDecimal sumOfDebts = debtCalculatorService.getSumOfDebts(debtorEntity);
        List<DebtEntity> oldestDebtEntities = debtCalculatorService.getListOfOldestDebts(debtorEntity);

        boolean paymentIsNotBiggerThanSumOfDebts = paymentAmount.compareTo(sumOfDebts) <= 0;
        boolean paymentIsBiggerThanSumOfDebts = paymentAmount.compareTo(sumOfDebts) > 0;

        if (!oldestDebtEntities.isEmpty() && paymentIsNotBiggerThanSumOfDebts) {
            createPaymentPlanDependingOnAmount(chosenDebtEntity, paymentAmount, oldestDebtEntities, paymentConfirmation);
        } else if (!oldestDebtEntities.isEmpty() && paymentIsBiggerThanSumOfDebts) {
            payAllDebts(debtorEntity, sumOfDebts, paymentConfirmation);
        }
    }

    private void createPaymentPlanDependingOnAmount(
            DebtEntity chosenDebtEntity,
            BigDecimal paymentAmount,
            List<DebtEntity> oldestDebtEntities,
            PaymentConfirmation paymentConfirmation) {

        BigDecimal debtLeftToPaid = debtCalculatorService.getDebtLeftToPaid(chosenDebtEntity);

        logger.info("Payments list for debt {} has been actualized.", chosenDebtEntity.getUuid());

        if (paymentAmount.compareTo(debtLeftToPaid) <= 0) {
            addPaymentToDebtsSetOfPayments(paymentAmount, chosenDebtEntity, paymentConfirmation);
        } else {
            payChosenDebtAndOthersByDate(paymentAmount, oldestDebtEntities, chosenDebtEntity, paymentConfirmation);
        }
    }

    private void payChosenDebtAndOthersByDate(BigDecimal paymentAmount, List<DebtEntity> oldestDebtEntities,
                                              DebtEntity chosenDebtEntity, PaymentConfirmation paymentConfirmation) {

        if (debtCalculatorService.getSumOfPayments(chosenDebtEntity).compareTo(chosenDebtEntity.getDebtAmount()) < 0) {
            BigDecimal restAmount = chosenDebtEntity.getDebtAmount().subtract(debtCalculatorService.getSumOfPayments(chosenDebtEntity));
            addPaymentToDebtsSetOfPayments(restAmount, chosenDebtEntity, paymentConfirmation);
            paymentAmount = paymentAmount.subtract(restAmount);
        }

        List<DebtEntity> debtsWithoutChosen = new ArrayList<>();
        for (DebtEntity d : oldestDebtEntities) {
            if (!d.getUuid().equals(chosenDebtEntity.getUuid())) {
                debtsWithoutChosen.add(d);
            }
        }
        payOldestDebts(paymentAmount, debtsWithoutChosen, paymentConfirmation);
    }

    private void payOldestDebts(BigDecimal paymentAmount, List<DebtEntity> oldestDebtEntities,
                                PaymentConfirmation paymentConfirmation) {

        for (int i = 0; i < oldestDebtEntities.size(); i++) {
            DebtEntity oldestDebtEntity = oldestDebtEntities.get(i);
            BigDecimal debtLeftToPaid = debtCalculatorService.getDebtLeftToPaid(oldestDebtEntity);

            logger.info("Payments list for debt {} has been actualized.", oldestDebtEntity.getUuid());

            if (paymentAmount.compareTo(debtLeftToPaid) <= 0) {
                addPaymentToDebtsSetOfPayments(paymentAmount, oldestDebtEntity, paymentConfirmation);
                break;
            } else {
                paymentAmount = addPaymentToDebtsSetOfPaymentsAndGetRemainingPaymentAmount(
                        paymentAmount, oldestDebtEntity, debtLeftToPaid, paymentConfirmation);
            }
        }
    }

    private void payAllDebts(DebtorEntity debtorEntity, BigDecimal sumOfDebts, PaymentConfirmation paymentConfirmation) {

        logger.info("All debts has been paid. Surplus: {}.",
                paymentConfirmation.getPaymentDeclaration().getPaymentAmount().subtract(sumOfDebts));

        List<DebtEntity> listOfDebtEntities = new ArrayList<>();
        debtorEntity.getDebtEntities().forEach(d -> listOfDebtEntities.add(d));

        for (int i = 0; i < listOfDebtEntities.size(); i++) {
            DebtEntity debtEntity = listOfDebtEntities.get(i);
            addNewPaymentToDebtsPayments(debtEntity, new PaymentEntity(
                    LocalDate.now(),
                    debtEntity.getDebtAmount().subtract(debtCalculatorService.getSumOfPayments(debtEntity)),
                    CreditCardEntity.convertFromCreditCard(paymentConfirmation.getCreditCard()),
                    paymentConfirmation.getClientId()));
        }
    }

    private void addPaymentToDebtsSetOfPayments(BigDecimal paymentAmount, DebtEntity debtEntity,
                                                PaymentConfirmation paymentConfirmation) {

        PaymentEntity paymentEntity = new PaymentEntity(LocalDate.now(), paymentAmount,
                CreditCardEntity.convertFromCreditCard(paymentConfirmation.getCreditCard()), paymentConfirmation.getClientId());

        addNewPaymentToDebtsPayments(debtEntity, paymentEntity);
    }

    private BigDecimal addPaymentToDebtsSetOfPaymentsAndGetRemainingPaymentAmount(
            BigDecimal paymentAmount, DebtEntity debtEntity, BigDecimal debtLeftToPaid, PaymentConfirmation paymentConfirmation) {

        PaymentEntity paymentEntity = new PaymentEntity(LocalDate.now(), debtLeftToPaid,
                CreditCardEntity.convertFromCreditCard(paymentConfirmation.getCreditCard()), paymentConfirmation.getClientId());

        addNewPaymentToDebtsPayments(debtEntity, paymentEntity);

        paymentAmount = paymentAmount.subtract(debtLeftToPaid);
        return paymentAmount;
    }

    private void addNewPaymentToDebtsPayments(DebtEntity debtEntity, PaymentEntity paymentEntity) {
        if (debtCalculatorService.getSumOfPayments(debtEntity).compareTo(debtEntity.getDebtAmount()) < 0) {
            debtEntity.addToSetOfPaymentEntities(paymentEntity);
            accountsRepository.save(debtEntity.getDebtorEntity());
        }
    }
}








