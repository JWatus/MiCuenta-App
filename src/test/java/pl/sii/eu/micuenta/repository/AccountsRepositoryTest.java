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
        assertThat(expected.getDebts().size()).isEqualTo(result.getDebts().size());

        Debt expectedDebt = expected.getDebts().iterator().next();
        Debt resultDebt = result.getDebts().iterator().next();

        assertThat(expectedDebt.getDebtAmount()).isEqualTo(resultDebt.getDebtAmount());
        assertThat(expectedDebt.getRepaymentDate()).isEqualTo(resultDebt.getRepaymentDate());
        assertThat(resultDebt.getPayments()).isNotEmpty();
        assertThat(expectedDebt.getPayments().size()).isEqualTo(resultDebt.getPayments().size());

        Payment expectedPayment = expectedDebt.getPayments().iterator().next();
        Payment resultPayment = resultDebt.getPayments().iterator().next();

        assertThat(expectedPayment.getPaymentAmount()).isEqualTo(resultPayment.getPaymentAmount());
        assertThat(expectedPayment.getPaymentDate()).isEqualTo(resultPayment.getPaymentDate());
        assertThat(expectedPayment.getCreditCard().getCcNumber()).isEqualTo(resultPayment.getCreditCard().getCcNumber());
        assertThat(expectedPayment.getCreditCard().getCvv()).isEqualTo(resultPayment.getCreditCard().getCvv());
        assertThat(expectedPayment.getCreditCard().getFirstName()).isEqualTo(resultPayment.getCreditCard().getFirstName());
        assertThat(expectedPayment.getCreditCard().getLastName()).isEqualTo(resultPayment.getCreditCard().getLastName());
    }
}