package pl.sii.eu.micuenta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sii.eu.micuenta.model.CreditCard;
import pl.sii.eu.micuenta.model.Debt;
import pl.sii.eu.micuenta.model.Debtor;
import pl.sii.eu.micuenta.model.Payment;
import pl.sii.eu.micuenta.model.form.PaymentForm;
import pl.sii.eu.micuenta.repository.AccountsRepository;
import pl.sii.eu.micuenta.service.CreditCardSerializer;
import pl.sii.eu.micuenta.service.DebtSerializer;
import pl.sii.eu.micuenta.service.DebtorSerializer;
import pl.sii.eu.micuenta.service.PaymentSerializer;

import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class AccountController {

    private static Logger logger = LoggerFactory.getLogger(AccountController.class);

    private ObjectMapper objectMapper;
    private AccountsRepository accountsRepository;

    public AccountController(AccountsRepository accountsRepository, ObjectMapper objectMapper) {
        this.accountsRepository = accountsRepository;
        this.objectMapper = objectMapper;
    }

    @RequestMapping(value = "/login", consumes = MediaType.APPLICATION_JSON, method = RequestMethod.POST)
    public ResponseEntity<String> login(@RequestBody Debtor debtor) {

        logger.info("Login attempt : {} {}.", debtor.getFirstName(), debtor.getLastName());

        Debtor foundDebtor = accountsRepository.findFirstBySsnAndFirstNameAndLastName(
                debtor.getSsn(),
                debtor.getFirstName(),
                debtor.getLastName());

        if (foundDebtor != null) {
            logger.info("Authorization passed.");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            logger.info("Authorization failed.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/balance/{ssn}", produces = MediaType.APPLICATION_JSON, method = RequestMethod.GET)
    public Debtor getBalance(@PathVariable String ssn) {

        Debtor debtor = accountsRepository.findFirstBySsn(ssn);

        if (debtor != null) {
            logger.info("User with ssn: {} has been found by system.", ssn);
            registerModule(objectMapper);
        } else {
            logger.info("User with ssn: {} has not been found by system.", ssn);
        }
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

    @RequestMapping(value = "/payment", consumes = MediaType.APPLICATION_JSON, method = RequestMethod.POST)
    public ResponseEntity<String> getPayment(@RequestBody PaymentForm paymentForm) {

        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Payment payment = paymentForm.getPayment();
        String ssn = paymentForm.getSsn();
        String debtUuid = paymentForm.getDebtUuid();

        if (payment.getPaymentAmount().compareTo(BigDecimal.ZERO) > 0)
            logger.info("Received payment: {}.", payment.getPaymentAmount().setScale(2, RoundingMode.HALF_EVEN));
        else {
            logger.info("Not valid payment amount.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Debtor debtor = accountsRepository.findFirstBySsn(ssn);

        


        Optional<Debt> oldestDebt = accountsRepository
                .findFirstBySsn(ssn)
                .getSetOfDebts()
                .stream()
                .sorted((a, b) -> a.getRepaymentDate().compareTo(b.getRepaymentDate()))
                .limit(1)
                .findFirst();


        for (Debt chosenDebt : debtor.getSetOfDebts()) {
            if (chosenDebt.getUuid().equals(debtUuid)) {

                chosenDebt.addToSetOfPayments(payment);

                logger.info("Set of payments for chosen debt have been actualized.");

                BigDecimal paymentAmount = payment.getPaymentAmount().setScale(2, RoundingMode.HALF_EVEN);
                BigDecimal chosenDebtAmount = chosenDebt.getDebtAmount().setScale(2, RoundingMode.HALF_EVEN);

                BigDecimal sumOfDebts = accountsRepository
                        .findFirstBySsn(ssn)
                        .getSetOfDebts()
                        .stream()
                        .map(Debt::getDebtAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .setScale(2, RoundingMode.HALF_EVEN);

                if (paymentAmount.compareTo(sumOfDebts) > 0) {
                    logger.info("All debts have been paid.");
                    for (Debt d : accountsRepository.findFirstBySsn(ssn).getSetOfDebts()) {
                        d.setDebtAmount(BigDecimal.ZERO);
                    }
                    response = new ResponseEntity<>(HttpStatus.OK);
                } else if (paymentAmount.compareTo(chosenDebtAmount) <= 0) {
                    logger.info("Debt with uuid {} has been actualized.", debtUuid);
                    chosenDebt.setDebtAmount(chosenDebtAmount.subtract(paymentAmount));
                    response = new ResponseEntity<>(HttpStatus.OK);
                } else {
                    chosenDebt.setDebtAmount(chosenDebtAmount.subtract(paymentAmount));
                    BigDecimal subtraction = paymentAmount.subtract(chosenDebtAmount);
                    logger.info("Debt with uuid {} has been actualized. After payment user has {} of surplus.", debtUuid, subtraction);
                    response = new ResponseEntity<>(HttpStatus.OK);
                }
            }
        }
        accountsRepository.save(debtor);
        return response;
    }
}








