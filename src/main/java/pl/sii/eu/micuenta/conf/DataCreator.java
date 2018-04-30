package pl.sii.eu.micuenta.conf;

import org.springframework.stereotype.Component;
import pl.sii.eu.micuenta.model.CreditCard;
import pl.sii.eu.micuenta.model.Debt;
import pl.sii.eu.micuenta.model.Debtor;
import pl.sii.eu.micuenta.model.Payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataCreator {

    public Debtor createDebtor() {

        CreditCard creditCardOne = new CreditCard("98978872537125", "109", "Jakub", "Watus");
        CreditCard creditCardTwo = new CreditCard("23457590909018", "235", "Jakub", "Watus");

        Payment paymentOne = new Payment(LocalDate.now(), BigDecimal.valueOf(500.00), creditCardOne);
        Payment paymentTwo = new Payment(LocalDate.now(), BigDecimal.valueOf(700.00), creditCardTwo);
        Payment paymentThree = new Payment(LocalDate.now(), BigDecimal.valueOf(700.00), creditCardTwo);
        Payment paymentFour = new Payment(LocalDate.now(), BigDecimal.valueOf(700.00), creditCardTwo);

        Set<Payment> setOfPaymentsOne = new HashSet<>();
        setOfPaymentsOne.add(paymentOne);
        setOfPaymentsOne.add(paymentTwo);
        Set<Payment> setOfPaymentsTwo = new HashSet<>();
        setOfPaymentsTwo.add(paymentThree);
        setOfPaymentsTwo.add(paymentFour);

        Debt debtOne = new Debt(BigDecimal.valueOf(50000.00), LocalDate.now(), setOfPaymentsOne);
        Debt debtTwo = new Debt(BigDecimal.valueOf(60000.00), LocalDate.now(), setOfPaymentsTwo);

        Set<Debt> setOfDebts = new HashSet<>();
        setOfDebts.add(debtOne);
        setOfDebts.add(debtTwo);

        return new Debtor("Jakub", "Watus", "980-122-111", setOfDebts);
    }
}
