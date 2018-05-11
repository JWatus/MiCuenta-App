package pl.sii.eu.micuenta.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sii.eu.micuenta.model.Debtor;
import pl.sii.eu.micuenta.model.form.PaymentConfirmation;
import pl.sii.eu.micuenta.model.form.PaymentDeclaration;
import pl.sii.eu.micuenta.model.form.PaymentPlan;
import pl.sii.eu.micuenta.service.controller.DataDebtorService;
import pl.sii.eu.micuenta.service.controller.PaymentService;

import javax.ws.rs.core.MediaType;

@Api(value = "AccountController",
        consumes = "debtor presence in MiCuenta application",
        produces = "debtor with list of debts",
        description = "AccountController class manages handling debtors and their debts")
@RestController
@RequestMapping("/")
public class AccountController {

    private final DataDebtorService dataDebtorService;
    private final PaymentService paymentService;

    public AccountController(DataDebtorService dataDebtorService, PaymentService paymentService) {
        this.dataDebtorService = dataDebtorService;
        this.paymentService = paymentService;
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

        return dataDebtorService.validateDebtorsData(debtor);
    }

    @ApiOperation(value = "Returns: debtor with list of debts")
    @RequestMapping(value = "/balance/{ssn}", produces = MediaType.APPLICATION_JSON, method = RequestMethod.GET)
    public Debtor getBalance(@PathVariable String ssn) {

        return dataDebtorService.getDebtorBySsn(ssn);
    }

    @RequestMapping(value = "/paymentplan", consumes = MediaType.APPLICATION_JSON, method = RequestMethod.POST)
    public PaymentPlan getPaymentPlan(@RequestBody PaymentDeclaration paymentDeclaration) {

        return paymentService.getPaymentPlanBasedOnPaymentDeclaration(paymentDeclaration);
    }

    @RequestMapping(value = "/paymentmethods/creditcard", consumes = MediaType.APPLICATION_JSON, method = RequestMethod.POST)
    public ResponseEntity<String> updatePayments(@RequestBody PaymentConfirmation paymentConfirmation) {

        return new ResponseEntity<>(HttpStatus.OK);
    }
}








