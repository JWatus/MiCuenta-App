package pl.sii.eu.micuenta.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pl.sii.eu.micuenta.conf.AppConfig;
import pl.sii.eu.micuenta.conf.DataCreator;
import pl.sii.eu.micuenta.model.Debtor;
import pl.sii.eu.micuenta.repository.AccountsRepository;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class AccountControllerTest {

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
        accountController = new AccountController(accountsRepository, new ObjectMapper());
        String userFirstName = "Jakub";
        String userLastName = "Watus";

        //when
        String result = accountController.getBalance("980-122-111");

        //then
        Assertions.assertThat(result.contains(userFirstName)).isTrue();
        Assertions.assertThat(result.contains(userLastName)).isTrue();
    }
}
