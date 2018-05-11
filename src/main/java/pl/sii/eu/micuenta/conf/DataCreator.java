package pl.sii.eu.micuenta.conf;

import org.springframework.stereotype.Component;
import pl.sii.eu.micuenta.model.CreditCard;
import pl.sii.eu.micuenta.model.Debt;
import pl.sii.eu.micuenta.model.Debtor;
import pl.sii.eu.micuenta.model.Payment;
import pl.sii.eu.micuenta.model.form.PaymentDeclaration;
import pl.sii.eu.micuenta.model.form.PaymentPlan;
import pl.sii.eu.micuenta.model.form.PlannedPayment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataCreator {

    public Debtor createDebtor() {

        CreditCard creditCardOne = new CreditCard("98978872537125", "109", "Jakub", "Watus", "MasterCard", LocalDate.now());
        CreditCard creditCardTwo = new CreditCard("23457590909018", "235", "Jakub", "Watus", "VISA", LocalDate.now());

        Payment paymentOne = new Payment(LocalDate.of(2009, 9, 9), BigDecimal.valueOf(500.00), creditCardOne, "Alivio");
        Payment paymentTwo = new Payment(LocalDate.of(2014, 1, 19), BigDecimal.valueOf(700.00), creditCardTwo, "Alivio");
        Payment paymentThree = new Payment(LocalDate.of(2015, 6, 6), BigDecimal.valueOf(700.00), creditCardTwo, "Alivio");
        Payment paymentFour = new Payment(LocalDate.of(2015, 7, 28), BigDecimal.valueOf(700.00), creditCardTwo, "Alivio");

        Set<Payment> setOfPaymentsOne = new HashSet<>();
        setOfPaymentsOne.add(paymentOne);
        setOfPaymentsOne.add(paymentTwo);
        Set<Payment> setOfPaymentsTwo = new HashSet<>();
        setOfPaymentsTwo.add(paymentThree);
        setOfPaymentsTwo.add(paymentFour);

        Debt debtOne = new Debt(BigDecimal.valueOf(50000.00), LocalDate.of(2017, 11, 16), setOfPaymentsOne, "111222333444", "speedLoan");
        Debt debtTwo = new Debt(BigDecimal.valueOf(60000.00), LocalDate.of(2018, 2, 6), setOfPaymentsTwo, "999888777666", "fastLoan");

        Set<Debt> setOfDebts = new HashSet<>();
        setOfDebts.add(debtOne);
        setOfDebts.add(debtTwo);

        return new Debtor("Jakub", "Watus", "980-122-111", setOfDebts);
    }

    public PaymentDeclaration createPaymentDeclaration() {

        BigDecimal paymentAmount = BigDecimal.valueOf(890.00);
        String ssn = "980-122-111";
        String debtUuid = "111222333444";

        return new PaymentDeclaration(paymentAmount, ssn, debtUuid);
    }

    public PaymentPlan createPaymentPlan() {

        PlannedPayment plannedPaymentOne = new PlannedPayment("111222333444", BigDecimal.valueOf(567.58));
        PlannedPayment plannedPaymentTwo = new PlannedPayment("999888777666", BigDecimal.valueOf(755.06));

        List<PlannedPayment> plannedPaymentList = new ArrayList<>();
        plannedPaymentList.add(plannedPaymentOne);
        plannedPaymentList.add(plannedPaymentTwo);

        String ssn = "980-122-111";
        String message = "Eldo";

        return new PaymentPlan(message, ssn, plannedPaymentList);
    }
}
