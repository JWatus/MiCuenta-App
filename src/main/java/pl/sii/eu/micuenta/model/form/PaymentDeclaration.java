package pl.sii.eu.micuenta.model.form;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class PaymentDeclaration {

    @ApiModelProperty(access = "private", name = "paymentAmount", example = "3000",
            value = "Debtor's amount of payment")
    private BigDecimal paymentAmount;
    @ApiModelProperty(access = "private", name = "ssn", example = "980-122-111",
            value = "Debtor's social security number")
    private String ssn;
    @ApiModelProperty(access = "private", name = "debtUuid", example = "111222333444",
            value = "Debtor's universal unique identifier")
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
}
