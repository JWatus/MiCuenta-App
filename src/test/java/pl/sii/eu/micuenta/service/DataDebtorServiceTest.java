package pl.sii.eu.micuenta.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import pl.sii.eu.micuenta.conf.AppConfig;
import pl.sii.eu.micuenta.conf.DataCreator;
import pl.sii.eu.micuenta.model.model_dto.Debtor;
import pl.sii.eu.micuenta.model.model_entity.DebtorEntity;
import pl.sii.eu.micuenta.repository.AccountsRepository;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@Transactional
@Rollback
@SpringBootTest
public class DataDebtorServiceTest {

    @Autowired
    private DataCreator dataCreator;

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private DataDebtorService dataDebtorService;

    @Test
    @Sql(scripts = "/sql_scripts/initial_db_state.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql_scripts/clean_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldResponseWithHttpStatusOkWhenUserWasSuccessfullyVerified() {

        //given
        DebtorEntity debtorEntity = dataCreator.createDebtor();
        accountsRepository.save(debtorEntity);

        DebtorEntity receivedDebtorEntity = dataCreator.createDebtor();

        //when
        ResponseEntity result = dataDebtorService.validateDebtorsData(Debtor.convertFromDebtorEntity(receivedDebtorEntity));

        //then
        assertThat(result).isEqualTo(new ResponseEntity(HttpStatus.OK));
    }

    @Test
    @Sql(scripts = "/sql_scripts/initial_db_state.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql_scripts/clean_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldResponseWithHttpStatusNotFoundWhenUserWasNotSuccessfullyVerified() {

        //given
        DebtorEntity debtorEntity = dataCreator.createDebtor();
        accountsRepository.save(debtorEntity);

        DebtorEntity receivedDebtorEntity = dataCreator.createDebtor();
        receivedDebtorEntity.setFirstName("Adam");
        receivedDebtorEntity.setLastName("Menethil");

        //when
        ResponseEntity result = dataDebtorService.validateDebtorsData(Debtor.convertFromDebtorEntity(receivedDebtorEntity));

        //then
        assertThat(result).isEqualTo(new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    @Test
    @Sql(scripts = "/sql_scripts/initial_db_state.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql_scripts/clean_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldReturnChosenDebtorWhenDataIsValid() {

        //given
        DebtorEntity debtor = dataCreator.createDebtor();
        accountsRepository.save(debtor);
        String userFirstName = "Jakub";
        String userLastName = "Watus";

        //when
        Debtor result = dataDebtorService.getDebtorBySsn("980-122-111");

        //then
        assertThat(result.getFirstName().equals(userFirstName));
        assertThat(result.getLastName().equals(userLastName));
    }
}
