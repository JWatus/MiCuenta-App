package pl.sii.eu.micuenta.conf;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import pl.sii.eu.micuenta.model.Debtor;
import pl.sii.eu.micuenta.repository.AccountsRepository;

import javax.annotation.PostConstruct;

@Configuration
@ComponentScan("pl.sii.eu.micuenta")
public class AppConfig {

    private DataCreator dataCreator;
    private AccountsRepository accountsRepository;

    public AppConfig(AccountsRepository accountsRepository, DataCreator dataCreator) {
        this.accountsRepository = accountsRepository;
        this.dataCreator = dataCreator;
    }

    @PostConstruct
    public void init() {
        Debtor debtor = dataCreator.createDebtor();
        accountsRepository.save(debtor);
    }
}


