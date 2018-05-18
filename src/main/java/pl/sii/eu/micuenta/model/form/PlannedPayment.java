package pl.sii.eu.micuenta.model.form;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class PlannedPayment {

    @ApiModelProperty(access = "private", name = "uuid", example = "111222333444",
            value = "Identificator of debt")
    String uuid;
    @ApiModelProperty(access = "private", name = "amountOfRepaymentDebt", example = "2750",
            value = "Part of debt which is paid")
    BigDecimal amountOfRepaymentDebt;

    public PlannedPayment() {
    }

    public PlannedPayment(String uuid, BigDecimal amountOfRepaymentDebt) {
        this.uuid = uuid;
        this.amountOfRepaymentDebt = amountOfRepaymentDebt;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public BigDecimal getAmountOfRepaymentDebt() {
        return amountOfRepaymentDebt;
    }

    public void setAmountOfRepaymentDebt(BigDecimal amountOfRepaymentDebt) {
        this.amountOfRepaymentDebt = amountOfRepaymentDebt;
    }
}
