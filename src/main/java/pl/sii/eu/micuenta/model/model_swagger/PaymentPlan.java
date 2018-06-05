package pl.sii.eu.micuenta.model.model_swagger;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

class PaymentPlan {

    @ApiModelProperty(access = "private", name = "message", example = "All debts have been paid.",
            value = "Message from API")
    private String message;
    @ApiModelProperty(access = "private", name = "ssn", example = "980-122-111",
            value = "Debtor's social security number")
    private String ssn;
    @ApiModelProperty(access = "private", name = "plannedPaymentList", example = "{}",
            value = "List of planned payments")
    private List<PlannedPayment> plannedPaymentList;
}
