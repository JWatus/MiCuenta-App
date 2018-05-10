package pl.sii.eu.micuenta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sii.eu.micuenta.model.Debt;
import pl.sii.eu.micuenta.model.Debtor;
import pl.sii.eu.micuenta.model.form.PaymentDeclaration;
import pl.sii.eu.micuenta.model.form.PaymentPlan;
import pl.sii.eu.micuenta.repository.AccountsRepository;
import pl.sii.eu.micuenta.service.controller.ControllerService;

import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Api(value = "AccountController",
        consumes = "debtor presence in MiCuenta application",
        produces = "debtor with list of debts",
        description = "AccountController class manages handling debtors and their debts")
@RestController
@RequestMapping("/")
public class AccountController {

    public static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    private ControllerService controllerService;
    private ObjectMapper objectMapper;
    private AccountsRepository accountsRepository;

    public AccountController(AccountsRepository accountsRepository, ObjectMapper objectMapper, ControllerService controllerService) {
        this.accountsRepository = accountsRepository;
        this.objectMapper = objectMapper;
        this.controllerService = controllerService;
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
            controllerService.registerModule(objectMapper);
        } else {
            logger.info("User with ssn: {} has not been found by system.", ssn);
        }
        return debtor;
    }

    @RequestMapping(value = "/paymentplan", consumes = MediaType.APPLICATION_JSON, method = RequestMethod.POST)
    public PaymentPlan getPaymentPlan(@RequestBody PaymentDeclaration paymentDeclaration) {

        BigDecimal paymentAmount = paymentDeclaration.getPaymentAmount().setScale(2, RoundingMode.HALF_EVEN);
        String ssn = paymentDeclaration.getSsn();
        String debtUuid = paymentDeclaration.getDebtUuid();

        PaymentPlan paymentPlan = new PaymentPlan();

        if (controllerService.notValidPaymentAmount(paymentAmount)) return null;

        Debtor debtor = accountsRepository.findFirstBySsn(ssn);

        if (debtUuid.isEmpty())
            paymentPlan = controllerService.handlingEmptyDebtId(debtor, paymentAmount);
        for (Debt chosenDebt : debtor.getSetOfDebts()) {
            if (chosenDebt.getUuid().equals(debtUuid))
                controllerService.handlingChosenDebtId(chosenDebt, paymentAmount);
        }
        return paymentPlan;
    }
}








