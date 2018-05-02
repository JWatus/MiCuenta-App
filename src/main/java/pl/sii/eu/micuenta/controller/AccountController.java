package pl.sii.eu.micuenta.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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

        Optional<Debtor> debtors = accountsRepository.findAll()
                .stream()
                .filter(u -> u.getSsn().equals(debtor.getSsn()))
                .filter(u -> u.getFirstName().equals(debtor.getFirstName()))
                .filter(u -> u.getLastName().equals(debtor.getLastName()))
                .findFirst();

        if (debtors.isPresent()) {
            logger.info("Authorization passed.");
            return new ResponseEntity(HttpStatus.OK);
        } else {
            logger.info("Authorization failed.");
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/balance/{ssn}", produces = MediaType.APPLICATION_JSON, method = RequestMethod.GET)
    public String getBalance(@PathVariable String ssn) throws JsonProcessingException {

        logger.info("User with ssn: {} has been found by system.", ssn);

        Debtor debtor = accountsRepository.findFirstBySsn(ssn);

        registerModule(objectMapper);

        return objectMapper.writeValueAsString(debtor);
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
    public void getPayment(@RequestBody PaymentForm paymentForm) {

        Payment payment = paymentForm.getPayment();
        String ssn = paymentForm.getSsn();
        String debtUuid = paymentForm.getDebtUuid();

        logger.info("Received payment: {}.", payment.getPaymentAmount());

        Debtor debtor = accountsRepository.findFirstBySsn(ssn);

        for (Debt chosenDebt : debtor.getSetOfDebts()) {
            if (chosenDebt.getUuid().equals(debtUuid)) {

                chosenDebt.addToSetOfPayments(payment);

                BigDecimal paymentAmount = payment.getPaymentAmount();
                BigDecimal chosenDebtAmount = chosenDebt.getDebtAmount();

                BigDecimal sumOfDebts = accountsRepository
                        .findFirstBySsn(ssn)
                        .getSetOfDebts()
                        .stream()
                        .map(debt -> debt.getDebtAmount())
                        .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

                if (paymentAmount.compareTo(sumOfDebts) == 1) {
                    logger.info("All debts have been paid.");
                    for (Debt d : accountsRepository.findFirstBySsn(ssn).getSetOfDebts()) {
                        d.setDebtAmount(BigDecimal.ZERO);
                    }
                } else if (paymentAmount.compareTo(chosenDebtAmount) != 1) {
                    logger.info("Debt with uuid {} has become actualized.", debtUuid);
                    chosenDebt.setDebtAmount(chosenDebtAmount.subtract(paymentAmount));
                } else {
                    chosenDebt.setDebtAmount(chosenDebtAmount.subtract(paymentAmount));
                    BigDecimal subtraction = paymentAmount.subtract(chosenDebtAmount);
                    logger.info("Debt with uuid {} has become actualized. After payment user has {} of surplus.", debtUuid, subtraction);
                }
            }
        }
        accountsRepository.save(debtor);
    }
}








