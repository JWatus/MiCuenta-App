package pl.sii.eu.micuenta.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pl.sii.eu.micuenta.conf.AppConfig;
import pl.sii.eu.micuenta.conf.DataCreator;
import pl.sii.eu.micuenta.model.Debt;
import pl.sii.eu.micuenta.model.Debtor;
import pl.sii.eu.micuenta.model.Payment;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@Rollback
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class AccountsRepositoryTest {

    @Autowired
    DataCreator dataCreator;

    @Autowired
    AccountsRepository accountsRepository;

    @Test
    public void shouldSaveDebtor() {

        //given
        Debtor expected = dataCreator.createDebtor();

        //when
        accountsRepository.deleteAll();
        accountsRepository.save(expected);

        //then
        Debtor result = accountsRepository.findAll().iterator().next();

        assertThat(expected.getFirstName()).isEqualTo(result.getFirstName());
        assertThat(expected.getLastName()).isEqualTo(result.getLastName());
        assertThat(expected.getSsn()).isEqualTo(result.getSsn());
        assertThat(expected.getSetOfDebts().size()).isEqualTo(result.getSetOfDebts().size());

        Debt expectedDebt = expected.getSetOfDebts().iterator().next();
        Debt resultDebt = result.getSetOfDebts().iterator().next();

        assertThat(expectedDebt.getDebtAmount()).isEqualTo(resultDebt.getDebtAmount());
        assertThat(expectedDebt.getRepaymentDate()).isEqualTo(resultDebt.getRepaymentDate());
        assertThat(resultDebt.getSetOfPayments()).isNotEmpty();
        assertThat(expectedDebt.getSetOfPayments().size()).isEqualTo(resultDebt.getSetOfPayments().size());

        Payment expectedPayment = expectedDebt.getSetOfPayments().iterator().next();
        Payment resultPayment = resultDebt.getSetOfPayments().iterator().next();

        assertThat(expectedPayment.getPaymentAmount()).isEqualTo(resultPayment.getPaymentAmount());
        assertThat(expectedPayment.getPaymentDate()).isEqualTo(resultPayment.getPaymentDate());
        assertThat(expectedPayment.getCreditCard().getCCNumber()).isEqualTo(resultPayment.getCreditCard().getCCNumber());
        assertThat(expectedPayment.getCreditCard().getCvv()).isEqualTo(resultPayment.getCreditCard().getCvv());
        assertThat(expectedPayment.getCreditCard().getFirstName()).isEqualTo(resultPayment.getCreditCard().getFirstName());
        assertThat(expectedPayment.getCreditCard().getLastName()).isEqualTo(resultPayment.getCreditCard().getLastName());
    }
}