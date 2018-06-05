package pl.sii.eu.micuenta.model.model_swagger;

import io.swagger.annotations.ApiModelProperty;
import pl.sii.eu.micuenta.model.model_entity.CreditCardEntity;

class PaymentConfirmation {

    @ApiModelProperty(access = "private", name = "paymentDeclaration", example = "{}",
            value = "Debtor's payment declaration")
    private PaymentDeclaration paymentDeclaration;
    @ApiModelProperty(access = "private", name = "clientId", example = "Alivio",
            value = "Client's id")
    private String clientId;
    @ApiModelProperty(access = "private", name = "creditCardEntity", example = "{}",
            value = "Payment from this Credit Card")
    private CreditCardEntity creditCardEntity;
}
