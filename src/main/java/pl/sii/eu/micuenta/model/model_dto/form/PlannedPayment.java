package pl.sii.eu.micuenta.model.model_dto.form;

import java.math.BigDecimal;
import java.util.Objects;

public class PlannedPayment {

    private String uuid;
    private BigDecimal amountOfRepaymentDebt;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlannedPayment)) return false;
        PlannedPayment that = (PlannedPayment) o;
        return Objects.equals(getUuid(), that.getUuid()) &&
                Objects.equals(getAmountOfRepaymentDebt(), that.getAmountOfRepaymentDebt());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getUuid(), getAmountOfRepaymentDebt());
    }
}
