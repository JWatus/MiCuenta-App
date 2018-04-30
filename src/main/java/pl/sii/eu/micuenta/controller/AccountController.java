package pl.sii.eu.micuenta.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.sii.eu.micuenta.model.CreditCard;
import pl.sii.eu.micuenta.model.Debt;
import pl.sii.eu.micuenta.model.Debtor;
import pl.sii.eu.micuenta.model.Payment;
import pl.sii.eu.micuenta.repository.AccountsRepository;
import pl.sii.eu.micuenta.service.CreditCardSerializer;
import pl.sii.eu.micuenta.service.DebtSerializer;
import pl.sii.eu.micuenta.service.DebtorSerializer;
import pl.sii.eu.micuenta.service.PaymentSerializer;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/micuenta")
public class AccountController {

    private static Logger LOG = LoggerFactory.getLogger(AccountController.class);

    private ObjectMapper objectMapper;
    private AccountsRepository accountsRepository;

    public AccountController(AccountsRepository accountsRepository, ObjectMapper objectMapper) {
        this.accountsRepository = accountsRepository;
        this.objectMapper = objectMapper;
    }

    @RequestMapping(value = "/login", consumes = MediaType.APPLICATION_JSON, method = RequestMethod.POST)
    public ResponseEntity<String> login(@RequestBody Debtor debtor) {

        LOG.info("Login attempt : {} {}", debtor.getFirstName(), debtor.getLastName());

        Optional<Debtor> debtors = accountsRepository.findAll()
                .stream()
                .filter(u -> u.getSsn().equals(debtor.getSsn()))
                .filter(u -> u.getFirstName().equals(debtor.getFirstName()))
                .filter(u -> u.getLastName().equals(debtor.getLastName()))
                .findFirst();

        if (debtors.isPresent()) {
            LOG.info("Authorization passed");
            return new ResponseEntity(HttpStatus.OK);
        } else {
            LOG.info("Authorization failed");
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/balance", produces = MediaType.APPLICATION_JSON, method = RequestMethod.GET)
    public String getBalance(@FormParam("ssn") String ssn) throws JsonProcessingException {

        LOG.info("Finding user with ssn: {}", ssn);

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
    public void getPayment(@RequestBody Payment payment, String ssn, Long debtId) {

        LOG.info("Received payment: {}", payment.getPaymentAmount());

        Debtor debtor = accountsRepository.findFirstBySsn(ssn);

        for (Debt chosenDebt : debtor.getSetOfDebts()) {
            if (chosenDebt.getId().equals(debtId)) {

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
                    LOG.info("All debts have been paid");
                    for (Debt d : accountsRepository.findFirstBySsn(ssn).getSetOfDebts()) {
                        d.setDebtAmount(BigDecimal.ZERO);
                    }
                } else if (paymentAmount.compareTo(chosenDebtAmount) != 1) {
                    LOG.info("Debt with id {} has become actualized", debtId);
                    chosenDebt.setDebtAmount(chosenDebtAmount.subtract(paymentAmount));
                } else {
                    LOG.info("Debt with id {} has become actualized", debtId);
                    chosenDebt.setDebtAmount(chosenDebtAmount.subtract(paymentAmount));
                    BigDecimal substraction = paymentAmount.subtract(chosenDebtAmount);
                    while (substraction.intValue() > 0) {
// todo
                        Debt nextDebt = debtor.getSetOfDebts().iterator().next();
                        nextDebt.setDebtAmount(nextDebt.getDebtAmount().subtract(substraction));
                        substraction = substraction.subtract(nextDebt.getDebtAmount());
                    }
                }
            }
        }
        accountsRepository.save(debtor);
    }
}








