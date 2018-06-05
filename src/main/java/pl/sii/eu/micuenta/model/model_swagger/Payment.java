package pl.sii.eu.micuenta.model.model_swagger;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

class Payment {

    private Long id;
    @ApiModelProperty(access = "private", name = "paymentDate", example = "2018-05-08", value = "Date of payment")
    private LocalDate paymentDate;
    @ApiModelProperty(access = "private", name = "paymentAmount", example = "50000.0", value = "Amount of Payment")
    private BigDecimal paymentAmount;
    @ApiModelProperty(access = "private", name = "clientId", example = "Alivio", value = "Client's name")
    private String clientId;
    @ApiModelProperty(access = "private", name = "creditCard", example = "{}", value = "Payment from this Credit Card")
    private CreditCard creditCard;
    @ApiModelProperty(access = "private", name = "debt", example = "{}", value = "Debt")
    private Debt debt;
}
