package pl.sii.eu.micuenta.model.form;

import java.math.BigDecimal;

public class PlannedPayment {

    String uuid;
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
