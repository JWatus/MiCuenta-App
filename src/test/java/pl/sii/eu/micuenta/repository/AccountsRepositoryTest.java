package pl.sii.eu.micuenta.repository;

import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import pl.sii.eu.micuenta.conf.AppConfig;
import pl.sii.eu.micuenta.conf.DataCreator;
import pl.sii.eu.micuenta.model.model_entity.DebtorEntity;

import javax.transaction.Transactional;

import static org.hamcrest.CoreMatchers.equalTo;

@Transactional
@Rollback
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class AccountsRepositoryTest {

    @Autowired
    AccountsRepository accountsRepository;

    @Test
    @Sql(scripts = "/sql_scripts/initial_db_state.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql_scripts/clean_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldSaveDebtor() {
        //given
        DebtorEntity expected = DataCreator.createDebtor();

        //when
        accountsRepository.save(expected);

        //then
        DebtorEntity actual = accountsRepository.findFirstBySsn(expected.getSsn());
        MatcherAssert.assertThat(expected, equalTo(actual));
    }
}