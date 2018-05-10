package pl.sii.eu.micuenta.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.stereotype.Service;
import pl.sii.eu.micuenta.controller.AccountController;
import pl.sii.eu.micuenta.model.CreditCard;
import pl.sii.eu.micuenta.model.Debt;
import pl.sii.eu.micuenta.model.Debtor;
import pl.sii.eu.micuenta.model.Payment;
import pl.sii.eu.micuenta.model.form.PaymentPlan;
import pl.sii.eu.micuenta.model.form.PlannedPayment;
import pl.sii.eu.micuenta.service.serializers.CreditCardSerializer;
import pl.sii.eu.micuenta.service.serializers.DebtSerializer;
import pl.sii.eu.micuenta.service.serializers.DebtorSerializer;
import pl.sii.eu.micuenta.service.serializers.PaymentSerializer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ControllerService {

    public void registerModule(ObjectMapper objectMapper) {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Debtor.class, new DebtorSerializer());
        module.addSerializer(Debt.class, new DebtSerializer());
        module.addSerializer(Payment.class, new PaymentSerializer());
        module.addSerializer(CreditCard.class, new CreditCardSerializer());
        objectMapper.registerModule(module);
    }

    public boolean notValidPaymentAmount(BigDecimal paymentAmount) {
        if (paymentAmount.compareTo(BigDecimal.ZERO) > 0)
            AccountController.logger.info("Received payment: {}.", paymentAmount.setScale(2, RoundingMode.HALF_EVEN));
        else {
            AccountController.logger.info("Not valid payment amount.");
            return true;
        }
        return false;
    }

    public PaymentPlan handlingEmptyDebtId(Debtor debtor, BigDecimal paymentAmount) {
        List<PlannedPayment> plannedPaymentList = new ArrayList<>();
        String ssn = debtor.getSsn();
        String message = "Your payment amount is  " + paymentAmount;
        PaymentPlan paymentPlan = new PaymentPlan(message, ssn, plannedPaymentList);

        BigDecimal sumOfDebts = getSumOfDebts(debtor);

        List<Debt> oldestDebts = getListOfOldestDebts(debtor);

        boolean isNotPaymentBiggerThanSumOfDebts = paymentAmount.compareTo(sumOfDebts) < 0;

        if (!oldestDebts.isEmpty() && isNotPaymentBiggerThanSumOfDebts)
            return payOldestDebts(paymentAmount, paymentPlan, oldestDebts);
        else
            return payAllDebts(debtor, paymentAmount, plannedPaymentList, sumOfDebts);
    }

    public void handlingChosenDebtId(Debt chosenDebt, BigDecimal paymentAmount) {

    }

    private PaymentPlan payOldestDebts(BigDecimal paymentAmount, PaymentPlan paymentPlan, List<Debt> oldestDebts) {

        AccountController.logger.info("PaymentPlan for {} has been actualized.", paymentPlan.getSsn());

        for (int i = 0; i < oldestDebts.size(); i++) {
            Debt oldestDebt = oldestDebts.get(i);
            BigDecimal sumOfPayments = getSumOfPayments(oldestDebt);

            BigDecimal debtAmount = oldestDebt.getDebtAmount();
            BigDecimal debtLeftToPaid = debtAmount.subtract(sumOfPayments);

            AccountController.logger.info("Payments list for debt {} is ready to be actualized.", oldestDebt.getUuid());

            if (paymentAmount.compareTo(debtLeftToPaid) <= 0) {
                addPaymentToPlan(paymentAmount, paymentPlan, oldestDebt);
                break;
            } else
                paymentAmount = addPaymentToPlanAndGetRemainingPaymentAmount(paymentAmount, paymentPlan, oldestDebt, debtLeftToPaid);
        }
        return paymentPlan;
    }

    private PaymentPlan payAllDebts(Debtor debtor, BigDecimal paymentAmount, List<PlannedPayment> plannedPaymentList, BigDecimal sumOfDebts) {
        AccountController.logger.info("All debts can been paid. Surplus: {}.", paymentAmount.subtract(sumOfDebts));
        PaymentPlan paymentPlan = new PaymentPlan
                ("All debts will be paid. You have " + paymentAmount.subtract(sumOfDebts) + " of surplus.",
                        debtor.getSsn(),
                        plannedPaymentList);
        for (Debt d : debtor.getSetOfDebts()) {
            BigDecimal sumOfPayments = getSumOfPayments(d);
            plannedPaymentList.add(new PlannedPayment(d.getUuid(), d.getDebtAmount().subtract(sumOfPayments)));
        }
        return paymentPlan;
    }

    private void addPaymentToPlan(BigDecimal paymentAmount, PaymentPlan paymentPlan, Debt oldestDebt) {
        List<PlannedPayment> plannedPaymentList = paymentPlan.getPlannedPaymentList();
        plannedPaymentList.add(new PlannedPayment(oldestDebt.getUuid(), paymentAmount));
        paymentPlan.setPlannedPaymentList(plannedPaymentList);
    }

    private BigDecimal addPaymentToPlanAndGetRemainingPaymentAmount(BigDecimal paymentAmount, PaymentPlan paymentPlan, Debt oldestDebt, BigDecimal debtLeftToPaid) {
        paymentPlan
                .getPlannedPaymentList()
                .add(new PlannedPayment(oldestDebt.getUuid(), debtLeftToPaid));
        paymentAmount = paymentAmount.subtract(debtLeftToPaid);
        return paymentAmount;
    }

    private List<Debt> getListOfOldestDebts(Debtor debtor) {
        return debtor
                .getSetOfDebts()
                .stream()
                .filter(d -> d.getDebtAmount().compareTo(BigDecimal.ZERO) > 0)
                .sorted(Comparator.comparing(Debt::getRepaymentDate))
                .collect(Collectors.toList());
    }

    private BigDecimal getSumOfDebts(Debtor debtor) {
        return debtor
                .getSetOfDebts()
                .stream()
                .map(Debt::getDebtAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getSumOfPayments(Debt oldestDebt) {
        return oldestDebt
                .getSetOfPayments()
                .stream()
                .map(Payment::getPaymentAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}