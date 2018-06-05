package pl.sii.eu.micuenta.model.model_swagger;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

class PlannedPayment {

    @ApiModelProperty(access = "private", name = "uuid", example = "111222333444",
            value = "Identificator of debt")
    String uuid;
    @ApiModelProperty(access = "private", name = "amountOfRepaymentDebt", example = "2750",
            value = "Part of debt which is paid")
    BigDecimal amountOfRepaymentDebt;
}
