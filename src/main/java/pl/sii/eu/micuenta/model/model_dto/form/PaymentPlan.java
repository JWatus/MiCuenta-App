package pl.sii.eu.micuenta.model.model_dto.form;

import java.util.List;
import java.util.Objects;

public class PaymentPlan {

    private String message;
    private String ssn;
    private List<PlannedPayment> plannedPaymentList;

    public PaymentPlan() {
    }

    public PaymentPlan(String message, String ssn, List<PlannedPayment> plannedPaymentList) {
        this.message = message;
        this.ssn = ssn;
        this.plannedPaymentList = plannedPaymentList;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentPlan)) return false;
        PaymentPlan that = (PaymentPlan) o;
        return Objects.equals(getMessage(), that.getMessage()) &&
                Objects.equals(getSsn(), that.getSsn()) &&
                Objects.equals(getPlannedPaymentList(), that.getPlannedPaymentList());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getMessage(), getSsn(), getPlannedPaymentList());
    }
}
