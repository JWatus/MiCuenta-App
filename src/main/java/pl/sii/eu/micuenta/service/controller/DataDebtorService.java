package pl.sii.eu.micuenta.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import pl.sii.eu.micuenta.model.CreditCard;
import pl.sii.eu.micuenta.model.Debt;
import pl.sii.eu.micuenta.model.Debtor;
import pl.sii.eu.micuenta.model.Payment;
import pl.sii.eu.micuenta.repository.AccountsRepository;
import pl.sii.eu.micuenta.service.serializers.CreditCardSerializer;
import pl.sii.eu.micuenta.service.serializers.DebtSerializer;
import pl.sii.eu.micuenta.service.serializers.DebtorSerializer;
import pl.sii.eu.micuenta.service.serializers.PaymentSerializer;

import java.util.Optional;

@Service
public class DataDebtorService {

    private ObjectMapper objectMapper;
    private AccountsRepository accountsRepository;

    public DataDebtorService(ObjectMapper objectMapper, AccountsRepository accountsRepository) {
        this.objectMapper = objectMapper;
        this.accountsRepository = accountsRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(DataDebtorService.class);

    public ResponseEntity<String> validateDebtorsData(@RequestBody Debtor debtor) {
        logger.info("Login attempt : {} {}.", debtor.getFirstName(), debtor.getLastName());

        Optional<Debtor> foundDebtor = accountsRepository.findFirstBySsnAndFirstNameAndLastName(
                debtor.getSsn(),
                debtor.getFirstName(),
                debtor.getLastName());

        if (foundDebtor.isPresent()) {
            logger.info("Authorization passed.");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            logger.info("Authorization failed.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public Debtor getDebtorBySsn(@PathVariable String ssn) {
        Debtor debtor = accountsRepository.findFirstBySsn(ssn);

        if (debtor == null) {
            logger.info("User with ssn: {} has not been found by system.", ssn);
            return new Debtor();
        }

        logger.info("User with ssn: {} has been found by system.", ssn);
        registerModule(objectMapper);
        return debtor;
    }

    private void registerModule(ObjectMapper objectMapper) {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Debtor.class, new DebtorSerializer());
        module.addSerializer(Debt.class, new DebtSerializer());
        module.addSerializer(Payment.class, new PaymentSerializer());
        module.addSerializer(CreditCard.class, new CreditCardSerializer());
        objectMapper.registerModule(module);
    }
}
