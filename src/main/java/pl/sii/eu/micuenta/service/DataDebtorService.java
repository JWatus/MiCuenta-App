package pl.sii.eu.micuenta.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import pl.sii.eu.micuenta.model.Debtor;
import pl.sii.eu.micuenta.repository.AccountsRepository;

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

    public String getDebtorBySsn(@PathVariable String ssn) throws JsonProcessingException {
        Debtor debtor = accountsRepository.findFirstBySsn(ssn);

        if (debtor == null) {
            logger.info("User with ssn: {} has not been found by system.", ssn);
            String response = "User with ssn: " + ssn + " has not been found by system.";
            return objectMapper.writeValueAsString(response);
        }
        logger.info("User with ssn: {} has been found by system.", ssn);

        return objectMapper.writeValueAsString(debtor);
    }
}
