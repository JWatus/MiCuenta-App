package pl.sii.eu.micuenta.service.controller;

import org.assertj.core.api.Assertions;
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
import pl.sii.eu.micuenta.model.form.PaymentPlan;
import pl.sii.eu.micuenta.model.form.PlannedPayment;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@Transactional
@Rollback
@SpringBootTest
public class ControllerServiceTest {

    @Autowired
    private ControllerService controllerService;

    @Autowired
    DataCreator dataCreator;

    @Test
    @Sql(scripts = "/sql_scripts/initial_db_state.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql_scripts/clean_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void paymentAmountShouldBeValid() {

        //given
        BigDecimal paymentAmount = BigDecimal.valueOf(500);

        //when
        boolean result = controllerService.notValidPaymentAmount(paymentAmount);

        //then
        Assertions.assertThat(result).isFalse();
    }

    @Test
    @Sql(scripts = "/sql_scripts/initial_db_state.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql_scripts/clean_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void paymentAmountShouldNotBeValid() {

        //given
        BigDecimal paymentAmount = BigDecimal.valueOf(-500);

        //when
        boolean result = controllerService.notValidPaymentAmount(paymentAmount);

        //then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    @Sql(scripts = "/sql_scripts/initial_db_state.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql_scripts/clean_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldReturnPaymentPlanWithOnePlannedPayment() {

        //given
        Debtor debtor = dataCreator.createDebtor();
        BigDecimal paymentAmount = BigDecimal.valueOf(800).setScale(2, RoundingMode.HALF_EVEN);

        //when
        PaymentPlan resultPaymentPlan = controllerService.handlingEmptyDebtId(debtor, paymentAmount);

        //then
        List<PlannedPayment> list = new ArrayList<>();
        list.add(new PlannedPayment("111222333444", BigDecimal.valueOf(800).setScale(2, RoundingMode.HALF_EVEN)));
        PaymentPlan expectedPaymentPlan = new PaymentPlan("Your payment amount is  800.00", "980-122-111", list);

        Assertions.assertThat(expectedPaymentPlan.getSsn())
                .isEqualTo(resultPaymentPlan.getSsn());
        Assertions.assertThat(expectedPaymentPlan.getMessage())
                .isEqualTo(resultPaymentPlan.getMessage());
        Assertions.assertThat(expectedPaymentPlan.getPlannedPaymentList().size())
                .isEqualTo(resultPaymentPlan.getPlannedPaymentList().size());
    }

    @Test
    @Sql(scripts = "/sql_scripts/initial_db_state.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql_scripts/clean_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldReturnPaymentPlanWithSeveralPlannedPayment() {

        //given
        Debtor debtor = dataCreator.createDebtor();
        BigDecimal paymentAmount = BigDecimal.valueOf(50800).setScale(2, RoundingMode.HALF_EVEN);

        //when
        PaymentPlan resultPaymentPlan = controllerService.handlingEmptyDebtId(debtor, paymentAmount);

        //then
        List<PlannedPayment> list = new ArrayList<>();
        list.add(new PlannedPayment("111222333444", BigDecimal.valueOf(48800).setScale(2, RoundingMode.HALF_EVEN)));
        list.add(new PlannedPayment("999888777666", BigDecimal.valueOf(2000).setScale(2, RoundingMode.HALF_EVEN)));
        PaymentPlan expectedPaymentPlan = new PaymentPlan("Your payment amount is  50800.00", "980-122-111", list);

        BigDecimal expectedRepaymentAmountForFirstDebt = list.get(0).getAmountOfRepaymentDebt();
        String expectedUuidForSecondDebt = list.get(1).getUuid();

        Assertions.assertThat(expectedPaymentPlan.getSsn())
                .isEqualTo(resultPaymentPlan.getSsn());
        Assertions.assertThat(expectedPaymentPlan.getMessage())
                .isEqualTo(resultPaymentPlan.getMessage());
        Assertions.assertThat(expectedPaymentPlan.getPlannedPaymentList().size())
                .isEqualTo(resultPaymentPlan.getPlannedPaymentList().size());
        Assertions.assertThat(expectedRepaymentAmountForFirstDebt.setScale(2, RoundingMode.HALF_EVEN))
                .isEqualTo(resultPaymentPlan.getPlannedPaymentList().get(0).getAmountOfRepaymentDebt().setScale(2, RoundingMode.HALF_EVEN));
        Assertions.assertThat(expectedUuidForSecondDebt)
                .isEqualTo(resultPaymentPlan.getPlannedPaymentList().get(1).getUuid());
    }

    @Test
    @Sql(scripts = "/sql_scripts/initial_db_state.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql_scripts/clean_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldPayAllDebts() {

        //given
        Debtor debtor = dataCreator.createDebtor();
        BigDecimal paymentAmount = BigDecimal.valueOf(150800).setScale(2, RoundingMode.HALF_EVEN);

        //when
        PaymentPlan resultPaymentPlan = controllerService.handlingEmptyDebtId(debtor, paymentAmount);

        //then
        List<PlannedPayment> list = new ArrayList<>();
        list.add(new PlannedPayment("111222333444", BigDecimal.valueOf(48800).setScale(2, RoundingMode.HALF_EVEN)));
        list.add(new PlannedPayment("999888777666", BigDecimal.valueOf(58600).setScale(2, RoundingMode.HALF_EVEN)));
        PaymentPlan expectedPaymentPlan = new PaymentPlan("All debts will be paid. You have 40800.00 of surplus.", "980-122-111", list);

        BigDecimal expectedRepaymentAmountForFirstDebt = list.get(0).getAmountOfRepaymentDebt();
        String expectedUuidForSecondDebt = list.get(1).getUuid();

        Assertions.assertThat(expectedPaymentPlan.getSsn())
                .isEqualTo(resultPaymentPlan.getSsn());
        Assertions.assertThat(expectedPaymentPlan.getMessage())
                .isEqualTo(resultPaymentPlan.getMessage());
        Assertions.assertThat(expectedPaymentPlan.getPlannedPaymentList().size())
                .isEqualTo(resultPaymentPlan.getPlannedPaymentList().size());
        Assertions.assertThat(expectedRepaymentAmountForFirstDebt.setScale(2, RoundingMode.HALF_EVEN))
                .isEqualTo(resultPaymentPlan.getPlannedPaymentList().get(0).getAmountOfRepaymentDebt().setScale(2, RoundingMode.HALF_EVEN));
        Assertions.assertThat(expectedUuidForSecondDebt)
                .isEqualTo(resultPaymentPlan.getPlannedPaymentList().get(1).getUuid());
    }
}
