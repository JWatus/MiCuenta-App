package pl.sii.eu.micuenta.model.form;

import pl.sii.eu.micuenta.model.Payment;

public class PaymentForm {

    private Payment payment;
    private String ssn;
    private String debtUuid;

    public PaymentForm() {
    }

    public PaymentForm(Payment payment, String ssn, String debtUuid) {
        this.payment = payment;
        this.ssn = ssn;
        this.debtUuid = debtUuid;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
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
