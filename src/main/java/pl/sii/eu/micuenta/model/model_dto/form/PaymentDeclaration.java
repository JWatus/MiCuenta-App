package pl.sii.eu.micuenta.model.model_dto.form;

import java.math.BigDecimal;
import java.util.Objects;

public class PaymentDeclaration {

    private BigDecimal paymentAmount;
    private String ssn;
    private String debtUuid;

    public PaymentDeclaration() {
    }

    public PaymentDeclaration(BigDecimal paymentAmount, String ssn, String debtUuid) {
        this.paymentAmount = paymentAmount;
        this.ssn = ssn;
        this.debtUuid = debtUuid;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getDebtUuid() {
        return debtUuid;
    }

    public void setDebtUuid(String debtUuid) {
        this.debtUuid = debtUuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentDeclaration)) return false;
        PaymentDeclaration that = (PaymentDeclaration) o;
        return Objects.equals(getPaymentAmount(), that.getPaymentAmount()) &&
                Objects.equals(getSsn(), that.getSsn()) &&
                Objects.equals(getDebtUuid(), that.getDebtUuid());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getPaymentAmount(), getSsn(), getDebtUuid());
    }
}
