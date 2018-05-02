package pl.sii.eu.micuenta.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pl.sii.eu.micuenta.conf.AppConfig;
import pl.sii.eu.micuenta.conf.DataCreator;
import pl.sii.eu.micuenta.model.Debt;
import pl.sii.eu.micuenta.model.Debtor;
import pl.sii.eu.micuenta.model.form.PaymentForm;
import pl.sii.eu.micuenta.repository.AccountsRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@Transactional
@Rollback
public class AccountControllerTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private DataCreator dataCreator;

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private AccountController accountController;

    @Test
    public void verifyUserPassed() {

        //given
        Debtor debtor = dataCreator.createDebtor();
        accountsRepository.deleteAll();
        accountsRepository.save(debtor);

        Debtor receivedDebtor = dataCreator.createDebtor();

        //when
        ResponseEntity result = accountController.login(receivedDebtor);

        //then
        Assertions.assertThat(result).isEqualTo(new ResponseEntity(HttpStatus.OK));
    }

    @Test
    public void verifyUserFailed() {

        //given
        Debtor debtor = dataCreator.createDebtor();
        accountsRepository.deleteAll();
        accountsRepository.save(debtor);

        Debtor receivedDebtor = new Debtor();

        //when
        ResponseEntity result = accountController.login(receivedDebtor);

        //then
        Assertions.assertThat(result).isEqualTo(new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    @Test
    public void getBalance() throws JsonProcessingException {

        //given
        Debtor debtor = dataCreator.createDebtor();
        accountsRepository.deleteAll();
        accountsRepository.save(debtor);
        String userFirstName = "Jakub";
        String userLastName = "Watus";

        //when
        String result = accountController.getBalance("980-122-111");

        //then
        Assertions.assertThat(result.contains(userFirstName)).isTrue();
        Assertions.assertThat(result.contains(userLastName)).isTrue();
    }

    @Test
//    @Sql(scripts = "/sql_scripts/initial_db_state.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    @Sql(scripts = "/sql_scripts/clean_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldUpdateSetOfPayments() {

        //given
        Debtor debtor = dataCreator.createDebtor();
        accountsRepository.deleteAll();
        accountsRepository.save(debtor);

        PaymentForm paymentForm = dataCreator.createPaymentForm();
        String debtUuid = "111222333444";

        //when
        accountController.getPayment(paymentForm);
        entityManager.flush();

        //then
        int expectedPaymentsSize = 3;
        int resultPaymentsSize = 0;
        for (Debt d : debtor.getSetOfDebts()) {
            if (d.getUuid().equals(debtUuid)) {
                resultPaymentsSize = d.getSetOfPayments().size();
            }
        }
        assertEquals(expectedPaymentsSize, resultPaymentsSize);
    }

    @Test
    public void shouldUpdateDebtAmount() {

        //given
        Debtor debtor = dataCreator.createDebtor();
        accountsRepository.deleteAll();
        accountsRepository.save(debtor);

        PaymentForm paymentForm = dataCreator.createPaymentForm();
        String debtUuid = "111222333444";

        //when
        accountController.getPayment(paymentForm);
        entityManager.flush();

        //then
        BigDecimal expectedAmount = BigDecimal.valueOf(49045.0);
        BigDecimal resultAmount = BigDecimal.ZERO;
        for (Debt d : debtor.getSetOfDebts()) {
            if (d.getUuid().equals(debtUuid)) {
                resultAmount = d.getDebtAmount();
            }
        }
        assertEquals(expectedAmount, resultAmount);
    }

    @Test
    public void shouldPaidAllDebts() {

        //given
        Debtor debtor = dataCreator.createDebtor();
        accountsRepository.deleteAll();
        accountsRepository.save(debtor);

        PaymentForm paymentForm = dataCreator.createPaymentForm();
        paymentForm.getPayment().setPaymentAmount(BigDecimal.valueOf(200000));

        //when
        accountController.getPayment(paymentForm);
        entityManager.flush();

        //then
        BigDecimal expectedAmount = BigDecimal.valueOf(0);
        BigDecimal resultAmount = BigDecimal.ZERO;

        for (Debt d : debtor.getSetOfDebts()) {
            resultAmount.add(d.getDebtAmount());
        }

        assertEquals(expectedAmount, resultAmount);
    }
}
