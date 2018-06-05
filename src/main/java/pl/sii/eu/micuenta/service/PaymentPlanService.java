package pl.sii.eu.micuenta.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pl.sii.eu.micuenta.model.model_entity.DebtEntity;
import pl.sii.eu.micuenta.model.model_entity.DebtorEntity;
import pl.sii.eu.micuenta.model.model_dto.form.PaymentDeclaration;
import pl.sii.eu.micuenta.model.model_dto.form.PaymentPlan;
import pl.sii.eu.micuenta.model.model_dto.form.PlannedPayment;
import pl.sii.eu.micuenta.repository.AccountsRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

@Service
public class PaymentPlanService {

    private DebtCalculatorService debtCalculatorService;
    private AccountsRepository accountsRepository;
    private ValidationService validationService;

    public PaymentPlanService(AccountsRepository accountsRepository,
                              DebtCalculatorService debtCalculatorService,
                              ValidationService validationService) {
        this.accountsRepository = accountsRepository;
        this.debtCalculatorService = debtCalculatorService;
        this.validationService = validationService;
    }

    private static final Logger logger = LoggerFactory.getLogger(PaymentPlanService.class);

    public PaymentPlan getPaymentPlanBasedOnPaymentDeclaration(@RequestBody PaymentDeclaration paymentDeclaration) {
        BigDecimal paymentAmount = paymentDeclaration.getPaymentAmount().setScale(2, RoundingMode.HALF_EVEN);
        String ssn = paymentDeclaration.getSsn();
        String debtUuid = paymentDeclaration.getDebtUuid();

        if (validationService.notValidPaymentAmount(paymentAmount, ssn)) {
            return new PaymentPlan("Payment amount is not valid.", ssn, emptyList());
        }

        PaymentPlan paymentPlan = new PaymentPlan("There is no debt with uuid " + debtUuid, ssn, emptyList());
        DebtorEntity debtorEntity = accountsRepository.findFirstBySsn(ssn);

        if (debtUuid.isEmpty()) {
            return handlingEmptyDebtId(debtorEntity, paymentAmount);
        }

        for (DebtEntity chosenDebtEntity : debtorEntity.getDebtEntities()) {
            if (chosenDebtEntity.getUuid().equals(debtUuid)) {
                return handlingChosenDebtId(chosenDebtEntity, paymentAmount, debtorEntity);
            }
        }
        return paymentPlan;
    }

    private PaymentPlan handlingEmptyDebtId(DebtorEntity debtorEntity, BigDecimal paymentAmount) {

        List<PlannedPayment> plannedPaymentList = new ArrayList<>();
        String ssn = debtorEntity.getSsn();
        String message = "Your payment amount is " + paymentAmount;
        PaymentPlan paymentPlan = new PaymentPlan(message, ssn, plannedPaymentList);

        BigDecimal sumOfDebts = debtCalculatorService.getSumOfDebts(debtorEntity);

        List<DebtEntity> oldestDebtEntities = debtCalculatorService.getListOfOldestDebts(debtorEntity);

        boolean paymentIsNotBiggerThanSumOfDebts = paymentAmount.compareTo(sumOfDebts) <= 0;
        boolean paymentIsBiggerThanSumOfDebts = paymentAmount.compareTo(sumOfDebts) > 0;

        if (!oldestDebtEntities.isEmpty() && paymentIsNotBiggerThanSumOfDebts) {
            return payOldestDebts(paymentAmount, paymentPlan, oldestDebtEntities);
        } else if (!oldestDebtEntities.isEmpty() && paymentIsBiggerThanSumOfDebts) {
            return payAllDebts(debtorEntity, paymentAmount, plannedPaymentList, sumOfDebts);
        } else {
            return new PaymentPlan("You don't have any debts to paid.", ssn, emptyList());
        }
    }

    private PaymentPlan handlingChosenDebtId(DebtEntity chosenDebtEntity, BigDecimal paymentAmount, DebtorEntity debtorEntity) {

        List<PlannedPayment> plannedPaymentList = new ArrayList<>();
        String ssn = debtorEntity.getSsn();
        String message = "Your payment amount is " + paymentAmount;
        PaymentPlan paymentPlan = new PaymentPlan(message, ssn, plannedPaymentList);

        BigDecimal sumOfDebts = debtCalculatorService.getSumOfDebts(debtorEntity);
        List<DebtEntity> oldestDebtEntities = debtCalculatorService.getListOfOldestDebts(debtorEntity);

        boolean paymentIsNotBiggerThanSumOfDebts = paymentAmount.compareTo(sumOfDebts) <= 0;
        boolean paymentIsBiggerThanSumOfDebts = paymentAmount.compareTo(sumOfDebts) > 0;

        if (!oldestDebtEntities.isEmpty() && paymentIsNotBiggerThanSumOfDebts) {
            return createPaymentPlanDependingOnAmount(chosenDebtEntity, paymentAmount, paymentPlan, oldestDebtEntities);
        } else if (!oldestDebtEntities.isEmpty() && paymentIsBiggerThanSumOfDebts) {
            return payAllDebts(debtorEntity, paymentAmount, plannedPaymentList, sumOfDebts);
        } else {
            return new PaymentPlan("You don't have any debts to paid.", ssn, emptyList());
        }
    }

    private PaymentPlan createPaymentPlanDependingOnAmount(DebtEntity chosenDebtEntity, BigDecimal paymentAmount, PaymentPlan paymentPlan, List<DebtEntity> oldestDebtEntities) {
        BigDecimal debtLeftToPaid = debtCalculatorService.getDebtLeftToPaid(chosenDebtEntity);

        logger.info("Payments list for debt {} is ready to be actualized.", chosenDebtEntity.getUuid());

        if (paymentAmount.compareTo(debtLeftToPaid) <= 0) {
            addPaymentToPlan(paymentAmount, paymentPlan, chosenDebtEntity);
            return paymentPlan;
        } else {
            return payChosenDebtAndOthersByDate(paymentAmount, paymentPlan, oldestDebtEntities, chosenDebtEntity);
        }
    }

    private PaymentPlan payChosenDebtAndOthersByDate(BigDecimal paymentAmount, PaymentPlan paymentPlan, List<DebtEntity> oldestDebtEntities, DebtEntity chosenDebtEntity) {

        BigDecimal restAmount = chosenDebtEntity.getDebtAmount().subtract(debtCalculatorService.getSumOfPayments(chosenDebtEntity));
        addPaymentToPlan(restAmount, paymentPlan, chosenDebtEntity);
        paymentAmount = paymentAmount.subtract(restAmount);

        List<DebtEntity> debtsWithoutChosen = new ArrayList<>();

        for (DebtEntity d : oldestDebtEntities) {
            if (!d.getUuid().equals(chosenDebtEntity.getUuid())) {
                debtsWithoutChosen.add(d);
            }
        }
        payOldestDebts(paymentAmount, paymentPlan, debtsWithoutChosen);
        return paymentPlan;
    }

    private PaymentPlan payOldestDebts(BigDecimal paymentAmount, PaymentPlan paymentPlan, List<DebtEntity> oldestDebtEntities) {

        logger.info("PaymentPlan for {} has been actualized.", paymentPlan.getSsn());

        for (int i = 0; i < oldestDebtEntities.size(); i++) {
            DebtEntity oldestDebtEntity = oldestDebtEntities.get(i);
            BigDecimal debtLeftToPaid = debtCalculatorService.getDebtLeftToPaid(oldestDebtEntity);

            logger.info("Payments list for debt {} is ready to be actualized.", oldestDebtEntity.getUuid());

            if (paymentAmount.compareTo(debtLeftToPaid) <= 0) {
                addPaymentToPlan(paymentAmount, paymentPlan, oldestDebtEntity);
                break;
            } else {
                paymentAmount = addPaymentToPlanAndGetRemainingPaymentAmount(paymentAmount, paymentPlan, oldestDebtEntity, debtLeftToPaid);
            }
        }
        return paymentPlan;
    }

    private PaymentPlan payAllDebts(DebtorEntity debtorEntity, BigDecimal paymentAmount, List<PlannedPayment> plannedPaymentList, BigDecimal sumOfDebts) {

        logger.info("All debts can been paid. Surplus: {}.", paymentAmount.subtract(sumOfDebts));

        PaymentPlan paymentPlan = new PaymentPlan
                ("All debts will be paid. You have " + paymentAmount.subtract(sumOfDebts) + " of surplus.", debtorEntity.getSsn(), plannedPaymentList);
        for (DebtEntity d : debtorEntity.getDebtEntities()) {
            BigDecimal sumOfPayments = debtCalculatorService.getSumOfPayments(d);
            BigDecimal remainingDebt = d.getDebtAmount().subtract(sumOfPayments);
            plannedPaymentList.add(new PlannedPayment(d.getUuid(), remainingDebt));
        }
        return paymentPlan;
    }

    private void addPaymentToPlan(BigDecimal paymentAmount, PaymentPlan paymentPlan, DebtEntity debtEntity) {
        List<PlannedPayment> plannedPaymentList = paymentPlan.getPlannedPaymentList();
        plannedPaymentList.add(new PlannedPayment(debtEntity.getUuid(), paymentAmount));
        paymentPlan.setPlannedPaymentList(plannedPaymentList);
    }

    private BigDecimal addPaymentToPlanAndGetRemainingPaymentAmount(BigDecimal paymentAmount, PaymentPlan paymentPlan, DebtEntity debtEntity, BigDecimal debtLeftToPaid) {
        paymentPlan
                .getPlannedPaymentList()
                .add(new PlannedPayment(debtEntity.getUuid(), debtLeftToPaid));
        paymentAmount = paymentAmount.subtract(debtLeftToPaid);
        return paymentAmount;
    }
}