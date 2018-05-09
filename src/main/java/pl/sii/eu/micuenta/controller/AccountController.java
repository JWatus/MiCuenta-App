package pl.sii.eu.micuenta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sii.eu.micuenta.conf.DataCreator;
import pl.sii.eu.micuenta.model.CreditCard;
import pl.sii.eu.micuenta.model.Debt;
import pl.sii.eu.micuenta.model.Debtor;
import pl.sii.eu.micuenta.model.Payment;
import pl.sii.eu.micuenta.model.form.PaymentDeclaration;
import pl.sii.eu.micuenta.model.form.PaymentPlan;
import pl.sii.eu.micuenta.model.form.PlannedPayment;
import pl.sii.eu.micuenta.repository.AccountsRepository;
import pl.sii.eu.micuenta.service.CreditCardSerializer;
import pl.sii.eu.micuenta.service.DebtSerializer;
import pl.sii.eu.micuenta.service.DebtorSerializer;
import pl.sii.eu.micuenta.service.PaymentSerializer;

import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "AccountController",
        consumes = "debtor presence in MiCuenta application",
        produces = "debtor with list of debts",
        description = "AccountController class manages handling debtors and their debts")
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

    @ApiOperation(value = "Returns: answer if debtor is present in MiCuenta application")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Debtor is found in MiCuenta"),
                    @ApiResponse(code = 404, message = "Debtor is not found in MiCuenta")
            }
    )
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

    @ApiOperation(value = "Returns: debtor with list of debts")
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

    @RequestMapping(value = "/paymentPlan", consumes = MediaType.APPLICATION_JSON, method = RequestMethod.POST)
    public PaymentPlan getPaymentPlan(@RequestBody PaymentDeclaration paymentDeclaration) {

        BigDecimal paymentAmount = paymentDeclaration.getPaymentAmount();
        String ssn = paymentDeclaration.getSsn();
        String debtUuid = paymentDeclaration.getDebtUuid();

        if (notValidPaymentAmount(paymentAmount)) return null;

        Debtor debtor = accountsRepository.findFirstBySsn(ssn);

        for (Debt chosenDebt : debtor.getSetOfDebts()) {
            if (debtUuid.isEmpty()) {
                return handlingEmptyDebtId(debtor, paymentAmount);
            } else if (chosenDebt.getUuid().equals(debtUuid)) {
                handlingChosenDebtId(chosenDebt, paymentAmount);
            }
        }

        DataCreator dataCreator = new DataCreator();
        return dataCreator.createPaymentPlan();
    }

    private boolean notValidPaymentAmount(BigDecimal paymentAmount) {
        if (paymentAmount.compareTo(BigDecimal.ZERO) > 0)
            logger.info("Received payment: {}.", paymentAmount.setScale(2, RoundingMode.HALF_EVEN));
        else {
            logger.info("Not valid payment amount.");
            return true;
        }
        return false;
    }

    private PaymentPlan handlingEmptyDebtId(Debtor debtor, BigDecimal paymentAmount) {

        PaymentPlan paymentPlan = new PaymentPlan();

        BigDecimal sumOfDebts = getSumOfDebts(debtor);

        List<Debt> oldestDebts = getListOfOldestDebts(debtor);

        boolean isPaymentBiggerThanSumOfDebts = paymentAmount.compareTo(sumOfDebts) > 0;

        if (!oldestDebts.isEmpty() && !isPaymentBiggerThanSumOfDebts) {
            return payOldestDebts(paymentAmount, paymentPlan, oldestDebts);
        } else {
            paymentPlan = new PaymentPlan("There is no more debts left to be paid.", debtor.getSsn(), null);
            logger.info("No more debts left to be paid.");
        }
        return paymentPlan;
    }

    private List<Debt> getListOfOldestDebts(Debtor debtor) {
        return debtor
                .getSetOfDebts()
                .stream()
                .filter(d -> d.getDebtAmount().compareTo(BigDecimal.ZERO) > 0)
                .sorted(Comparator.comparing(Debt::getRepaymentDate))
                .collect(Collectors.toList());
    }

    private BigDecimal getSumOfDebts(Debtor debtor) {
        return debtor
                .getSetOfDebts()
                .stream()
                .map(Debt::getDebtAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private PaymentPlan payOldestDebts(BigDecimal paymentAmount, PaymentPlan paymentPlan, List<Debt> oldestDebts) {
        for (int i = 0; i < oldestDebts.size(); i++) {
            Debt oldestDebt = oldestDebts.get(i);
            BigDecimal sumOfPayments = getSumOfPayments(oldestDebt);

            BigDecimal debtAmount = oldestDebt.getDebtAmount();
            BigDecimal debtLeftToPaid = debtAmount.subtract(sumOfPayments);

            if (paymentAmount.compareTo(debtLeftToPaid) <= 0) {
                paymentPlan
                        .getPlannedPaymentList()
                        .add(new PlannedPayment(oldestDebt.getUuid(), paymentAmount));
                break;
            } else {
                paymentPlan
                        .getPlannedPaymentList()
                        .add(new PlannedPayment(oldestDebt.getUuid(), paymentAmount));
                paymentAmount = paymentAmount.subtract(debtLeftToPaid);
            }
            logger.info("Set of payments for debt {} have been actualized.", oldestDebt.getUuid());
        }
        return paymentPlan;
    }

    private BigDecimal getSumOfPayments(Debt oldestDebt) {
        return oldestDebt
                        .getSetOfPayments()
                        .stream()
                        .map(Payment::getPaymentAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void handlingChosenDebtId(Debt chosenDebt, BigDecimal paymentAmount) {

    }
}








