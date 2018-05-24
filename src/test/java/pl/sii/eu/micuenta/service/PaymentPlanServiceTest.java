package pl.sii.eu.micuenta.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import pl.sii.eu.micuenta.conf.AppConfig;
import pl.sii.eu.micuenta.conf.DataCreator;
import pl.sii.eu.micuenta.model.Debtor;
import pl.sii.eu.micuenta.model.form.PaymentDeclaration;
import pl.sii.eu.micuenta.model.form.PaymentPlan;
import pl.sii.eu.micuenta.model.form.PlannedPayment;
import pl.sii.eu.micuenta.repository.AccountsRepository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import static java.util.Collections.emptyList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@Transactional
@Rollback
@SpringBootTest
public class PaymentPlanServiceTest {

    @Autowired
    private PaymentPlanService paymentPlanService;
    @Autowired
    private AccountsRepository accountsRepository;
    @Autowired
    private DataCreator dataCreator;

    @Test
    @Sql(scripts = "/sql_scripts/initial_db_state.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql_scripts/clean_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldGetProperMessageWhenPaymentAmountIsNotValid() {

        //given
        PaymentDeclaration paymentDeclaration = new PaymentDeclaration(BigDecimal.valueOf(-4560), "980-122-111", "");

        //when
        PaymentPlan result = paymentPlanService.getPaymentPlanBasedOnPaymentDeclaration(paymentDeclaration);

        //then
        String message = "Payment amount is not valid.";
        PaymentPlan expected = new PaymentPlan(message, null, null);

        assertThat(expected.getMessage()).isEqualTo(result.getMessage());
    }

    @Test
    @Sql(scripts = "/sql_scripts/initial_db_state.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql_scripts/clean_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldGetPaymentPlanBasedOnPaymentDeclarationWhenDebtUuidIsEmpty() {

        //given
        Debtor debtor = dataCreator.createDebtor();
        accountsRepository.save(debtor);
        PaymentDeclaration paymentDeclaration = new PaymentDeclaration(
                BigDecimal.valueOf(450).setScale(2, RoundingMode.HALF_EVEN),
                "980-122-111", "");

        //when
        PaymentPlan result = paymentPlanService.getPaymentPlanBasedOnPaymentDeclaration(paymentDeclaration);

        //then
        String message = "Your payment amount is 450.00";
        String ssn = "980-122-111";
        List<PlannedPayment> plannedPaymentList = new ArrayList<>();
        PlannedPayment plannedPaymentOne = new PlannedPayment("CRTP/909088",
                BigDecimal.valueOf(450).setScale(2, RoundingMode.HALF_EVEN));
        plannedPaymentList.add(plannedPaymentOne);

        PaymentPlan expected = new PaymentPlan(message, ssn, plannedPaymentList);

        assertThat(expected.getMessage()).isEqualTo(result.getMessage());
        assertThat(expected.getSsn()).isEqualTo(result.getSsn());
    }

    @Test
    @Sql(scripts = "/sql_scripts/initial_db_state.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql_scripts/clean_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldGetPaymentPlanBasedOnPaymentDeclarationWhenDebtUuidIsEmptyAndPaymentAmountIsBiggerThatOperatedDebt() {

        //given
        Debtor debtor = dataCreator.createDebtor();
        accountsRepository.save(debtor);
        PaymentDeclaration paymentDeclaration = new PaymentDeclaration(
                BigDecimal.valueOf(55550).setScale(2, RoundingMode.HALF_EVEN),
                "980-122-111", "");

        //when
        PaymentPlan result = paymentPlanService.getPaymentPlanBasedOnPaymentDeclaration(paymentDeclaration);

        //then
        String message = "Your payment amount is 55550.00";
        String ssn = "980-122-111";
        List<PlannedPayment> plannedPaymentList = new ArrayList<>();
        PlannedPayment plannedPaymentOne = new PlannedPayment("CRTP/909088",
                BigDecimal.valueOf(48800).setScale(2, RoundingMode.HALF_EVEN));
        PlannedPayment plannedPaymentTwo = new PlannedPayment("KIGT/116256",
                BigDecimal.valueOf(6750).setScale(2, RoundingMode.HALF_EVEN));
        plannedPaymentList.add(plannedPaymentOne);
        plannedPaymentList.add(plannedPaymentTwo);

        PaymentPlan expected = new PaymentPlan(message, ssn, plannedPaymentList);

        assertThat(expected.getMessage()).isEqualTo(result.getMessage());
        assertThat(expected.getSsn()).isEqualTo(result.getSsn());
    }

    @Test
    @Sql(scripts = "/sql_scripts/initial_db_state.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql_scripts/clean_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldGetPaymentPlanBasedOnPaymentDeclarationWhenDebtUuidIsEmptyAndPaymentAmountIsBiggerAllDebts() {

        //given
        Debtor debtor = dataCreator.createDebtor();
        accountsRepository.save(debtor);
        PaymentDeclaration paymentDeclaration = new PaymentDeclaration(
                BigDecimal.valueOf(155550).setScale(2, RoundingMode.HALF_EVEN),
                "980-122-111", "");

        //when
        PaymentPlan result = paymentPlanService.getPaymentPlanBasedOnPaymentDeclaration(paymentDeclaration);

        //then
        String message = "All debts will be paid. You have 6550.00 of surplus.";
        String ssn = "980-122-111";
        List<PlannedPayment> plannedPaymentList = new ArrayList<>();
        PlannedPayment plannedPaymentOne = new PlannedPayment("CRTP/909088",
                BigDecimal.valueOf(48800).setScale(2, RoundingMode.HALF_EVEN));
        PlannedPayment plannedPaymentTwo = new PlannedPayment("KIGT/116256",
                BigDecimal.valueOf(58600).setScale(2, RoundingMode.HALF_EVEN));
        plannedPaymentList.add(plannedPaymentOne);
        plannedPaymentList.add(plannedPaymentTwo);

        PaymentPlan expected = new PaymentPlan(message, ssn, plannedPaymentList);

        assertThat(expected.getMessage()).isEqualTo(result.getMessage());
        assertThat(expected.getSsn()).isEqualTo(result.getSsn());
    }

    @Test
    @Sql(scripts = "/sql_scripts/initial_db_state.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql_scripts/clean_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldGetMessageThatThereIsNoDebts() {

        //given
        Debtor debtor = dataCreator.createDebtor();
        debtor.setSetOfDebts(new HashSet<>());
        accountsRepository.save(debtor);
        PaymentDeclaration paymentDeclaration = new PaymentDeclaration(
                BigDecimal.valueOf(155550).setScale(2, RoundingMode.HALF_EVEN),
                "980-122-111", "");

        //when
        PaymentPlan result = paymentPlanService.getPaymentPlanBasedOnPaymentDeclaration(paymentDeclaration);

        //then
        String message = "You don't have any debts to paid.";
        String ssn = "980-122-111";

        PaymentPlan expected = new PaymentPlan(message, ssn, emptyList());

        assertThat(expected.getMessage()).isEqualTo(result.getMessage());
        assertThat(expected.getSsn()).isEqualTo(result.getSsn());
    }
}
