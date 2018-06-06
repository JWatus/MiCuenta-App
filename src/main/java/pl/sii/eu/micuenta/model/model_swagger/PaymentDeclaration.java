package pl.sii.eu.micuenta.model.model_swagger;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

class PaymentDeclaration {

    @ApiModelProperty(access = "private", name = "paymentAmount", example = "3000",
            value = "Debtor's amount of payment")
    private BigDecimal paymentAmount;
    @ApiModelProperty(access = "private", name = "ssn", example = "980-122-111",
            value = "Debtor's social security number")
    private String ssn;
    @ApiModelProperty(access = "private", name = "debtUuid", example = "111222333444",
            value = "Debtor's universal unique identifier")
    private String debtUuid;
}
