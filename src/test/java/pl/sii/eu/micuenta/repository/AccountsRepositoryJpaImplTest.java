package pl.sii.eu.micuenta.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import pl.sii.eu.micuenta.conf.AppConfig;
import pl.sii.eu.micuenta.conf.DataCreator;
import pl.sii.eu.micuenta.model.Debtor;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


@Transactional
@Rollback
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class AccountsRepositoryJpaImplTest {
    @Autowired
    DataCreator dataCreator;
    @Autowired
    AccountsRepository accountsRepository;
    @Autowired
    AccountsRepositoryJpaImpl accountRepositorySQL;

    @Test
    @Sql(scripts = "/sql_scripts/initial_db_state.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql_scripts/clean_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldReturnDebtorBySsn() {
        //given
        Debtor expected = dataCreator.createDebtor();

        //when
        accountsRepository.save(expected);

        //than
        Debtor actual = accountRepositorySQL.findFirstBySsn(expected.getSsn());
        assertThat(expected, equalTo(actual));
    }

    @Test
    @Sql(scripts = "/sql_scripts/initial_db_state.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql_scripts/clean_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldReturnDebtorBySsnAndFirstNameAndLastName() {
        //given
        Debtor expected = dataCreator.createDebtor();

        //when
        accountsRepository.save(expected);
        Optional<Debtor> actual = accountRepositorySQL.findFirstBySsnAndFirstNameAndLastName("980-122-111", "Jakub", "Watus");

        //than
        assertThat(expected, equalTo(actual.get()));
    }

    @Test
    @Sql(scripts = "/sql_scripts/initial_db_state.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql_scripts/clean_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldReturnNullDebtorWhenFetchBySsnAndFirstNameAndLastName() {
        //given
        Debtor expected = dataCreator.createDebtor();

        //when
        accountsRepository.deleteAll();
        accountsRepository.save(expected);
        Optional<Debtor> actual = accountRepositorySQL.findFirstBySsnAndFirstNameAndLastName("notExisting", "notExisting", "notExisting");

        //than
        assertThat(actual, is(Optional.empty()));
    }
}