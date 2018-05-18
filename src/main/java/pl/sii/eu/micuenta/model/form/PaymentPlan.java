package pl.sii.eu.micuenta.model.form;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class PaymentPlan {

    @ApiModelProperty(access = "private", name = "message", example = "All debts have been paid.",
            value = "Message from API")
    private String message;
    @ApiModelProperty(access = "private", name = "ssn", example = "980-122-111",
            value = "Debtor's social security number")
    private String ssn;
    @ApiModelProperty(access = "private", name = "plannedPaymentList", example = "{}",
            value = "List of planned payments")
    private List<PlannedPayment> plannedPaymentList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public List<PlannedPayment> getPlannedPaymentList() {
        return plannedPaymentList;
    }

    public void setPlannedPaymentList(List<PlannedPayment> plannedPaymentList) {
        this.plannedPaymentList = plannedPaymentList;
    }

    public PaymentPlan() {
    }

    public PaymentPlan(String message, String ssn, List<PlannedPayment> plannedPaymentList) {
        this.message = message;
        this.ssn = ssn;
        this.plannedPaymentList = plannedPaymentList;
    }
}
