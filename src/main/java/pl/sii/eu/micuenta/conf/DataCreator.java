package pl.sii.eu.micuenta.conf;

import org.springframework.stereotype.Component;
import pl.sii.eu.micuenta.model.model_entity.CreditCardEntity;
import pl.sii.eu.micuenta.model.model_entity.DebtEntity;
import pl.sii.eu.micuenta.model.model_entity.DebtorEntity;
import pl.sii.eu.micuenta.model.model_entity.PaymentEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataCreator {

    public static DebtorEntity createDebtor() {

        CreditCardEntity creditCardEntityOne = new CreditCardEntity("5199863120932752", "109", "Andy",
                "Larkin", "MasterCard", LocalDate.of(2025, 2, 14));
        CreditCardEntity creditCardEntityTwo = new CreditCardEntity("4556611605570880", "235", "Stanley",
                "Ipkins", "VISA", LocalDate.of(2020, 7, 7));

        PaymentEntity paymentEntityOne = new PaymentEntity(LocalDate.of(2009, 9, 9),
                BigDecimal.valueOf(500.00).setScale(2, RoundingMode.HALF_EVEN), creditCardEntityOne, "Alivio");
        PaymentEntity paymentEntityTwo = new PaymentEntity(LocalDate.of(2014, 1, 19),
                BigDecimal.valueOf(700.00).setScale(2, RoundingMode.HALF_EVEN), creditCardEntityTwo, "Velka");
        PaymentEntity paymentEntityThree = new PaymentEntity(LocalDate.of(2015, 6, 6),
                BigDecimal.valueOf(700.00).setScale(2, RoundingMode.HALF_EVEN), creditCardEntityTwo, "Alivio");
        PaymentEntity paymentEntityFour = new PaymentEntity(LocalDate.of(2015, 7, 28),
                BigDecimal.valueOf(700.00).setScale(2, RoundingMode.HALF_EVEN), creditCardEntityTwo, "Velka");
        PaymentEntity paymentEntityFive = new PaymentEntity(LocalDate.of(2015, 7, 28),
                BigDecimal.valueOf(150.00).setScale(2, RoundingMode.HALF_EVEN), creditCardEntityTwo, "Alivio");
        PaymentEntity paymentEntitySix = new PaymentEntity(LocalDate.of(2015, 7, 28),
                BigDecimal.valueOf(671.00).setScale(2, RoundingMode.HALF_EVEN), creditCardEntityTwo, "Velka");

        Set<PaymentEntity> setOfPaymentsOne = new HashSet<>();
        setOfPaymentsOne.add(paymentEntityOne);
        setOfPaymentsOne.add(paymentEntityTwo);
        Set<PaymentEntity> setOfPaymentsTwo = new HashSet<>();
        setOfPaymentsTwo.add(paymentEntityThree);
        setOfPaymentsTwo.add(paymentEntityFour);
        Set<PaymentEntity> setOfPaymentsThree = new HashSet<>();
        setOfPaymentsThree.add(paymentEntityFive);
        Set<PaymentEntity> setOfPaymentsFour = new HashSet<>();
        setOfPaymentsFour.add(paymentEntitySix);

        DebtEntity debtEntityOne = new DebtEntity(BigDecimal.valueOf(50000.00).setScale(2, RoundingMode.HALF_EVEN), LocalDate.of(2017, 11, 16),
                setOfPaymentsOne, "CRTP/909088", "Cross Finance");
        DebtEntity debtEntityTwo = new DebtEntity(BigDecimal.valueOf(60000.00).setScale(2, RoundingMode.HALF_EVEN), LocalDate.of(2018, 2, 6),
                setOfPaymentsTwo, "KIGT/116256", "India Lends");
        DebtEntity debtEntityThree = new DebtEntity(BigDecimal.valueOf(4000.00).setScale(2, RoundingMode.HALF_EVEN), LocalDate.of(1999, 12, 1),
                setOfPaymentsThree, "PLWT/871422", "King Of Kash");
        DebtEntity debtEntityFour = new DebtEntity(BigDecimal.valueOf(35000.00).setScale(2, RoundingMode.HALF_EVEN), LocalDate.of(1974, 6, 6),
                setOfPaymentsFour, "ADWR/595501", "Opp Loans");

        Set<DebtEntity> setOfDebtEntities = new HashSet<>();
        setOfDebtEntities.add(debtEntityOne);
        setOfDebtEntities.add(debtEntityTwo);
        setOfDebtEntities.add(debtEntityThree);
        setOfDebtEntities.add(debtEntityFour);

        return new DebtorEntity("Jaime", "Karren", "980-122-111", setOfDebtEntities);
    }
}
