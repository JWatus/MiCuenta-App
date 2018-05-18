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

        CreditCard creditCardOne = new CreditCard("5199863120932752", "109", "Andy",
                "Larkin", "MasterCard", LocalDate.of(2025, 2, 14));
        CreditCard creditCardTwo = new CreditCard("4556611605570880", "235", "Stanley",
                "Ipkins", "VISA", LocalDate.of(2020, 7, 7));

        Payment paymentOne = new Payment(LocalDate.of(2009, 9, 9),
                BigDecimal.valueOf(500.00), creditCardOne, "Alivio");
        Payment paymentTwo = new Payment(LocalDate.of(2014, 1, 19),
                BigDecimal.valueOf(700.00), creditCardTwo, "Velka");
        Payment paymentThree = new Payment(LocalDate.of(2015, 6, 6),
                BigDecimal.valueOf(700.00), creditCardTwo, "Alivio");
        Payment paymentFour = new Payment(LocalDate.of(2015, 7, 28),
                BigDecimal.valueOf(700.00), creditCardTwo, "Velka");
        Payment paymentFive = new Payment(LocalDate.of(2015, 7, 28),
                BigDecimal.valueOf(150.00), creditCardTwo, "Alivio");
        Payment paymentSix = new Payment(LocalDate.of(2015, 7, 28),
                BigDecimal.valueOf(671.00), creditCardTwo, "Velka");

        Set<Payment> setOfPaymentsOne = new HashSet<>();
        setOfPaymentsOne.add(paymentOne);
        setOfPaymentsOne.add(paymentTwo);
        Set<Payment> setOfPaymentsTwo = new HashSet<>();
        setOfPaymentsTwo.add(paymentThree);
        setOfPaymentsTwo.add(paymentFour);
        Set<Payment> setOfPaymentsThree = new HashSet<>();
        setOfPaymentsThree.add(paymentFive);
        Set<Payment> setOfPaymentsFour = new HashSet<>();
        setOfPaymentsFour.add(paymentSix);

        Debt debtOne = new Debt(BigDecimal.valueOf(50000.00), LocalDate.of(2017, 11, 16),
                setOfPaymentsOne, "CRTP/909088", "Cross Finance");
        Debt debtTwo = new Debt(BigDecimal.valueOf(60000.00), LocalDate.of(2018, 2, 6),
                setOfPaymentsTwo, "KIGT/116256", "India Lends");
        Debt debtThree = new Debt(BigDecimal.valueOf(4000.00), LocalDate.of(1999, 12, 1),
                setOfPaymentsThree, "PLWT/871422", "King Of Kash");
        Debt debtFour = new Debt(BigDecimal.valueOf(35000.00), LocalDate.of(1974, 6, 6),
                setOfPaymentsFour, "ADWR/595501", "Opp Loans");

        Set<Debt> setOfDebts = new HashSet<>();
        setOfDebts.add(debtOne);
        setOfDebts.add(debtTwo);
        setOfDebts.add(debtThree);
        setOfDebts.add(debtFour);

        return new Debtor("Jakub", "Watus", "980-122-111", setOfDebts);
    }
}
