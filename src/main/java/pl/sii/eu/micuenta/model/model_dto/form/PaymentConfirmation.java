package pl.sii.eu.micuenta.model.model_dto.form;

import pl.sii.eu.micuenta.model.model_dto.CreditCard;

import java.util.Objects;

public class PaymentConfirmation {

    private PaymentDeclaration paymentDeclaration;
    private String clientId;
    private CreditCard creditCard;

    public PaymentConfirmation() {
    }

    public PaymentConfirmation(PaymentDeclaration paymentDeclaration, String clientId, CreditCard creditCard) {
        this.paymentDeclaration = paymentDeclaration;
        this.clientId = clientId;
        this.creditCard = creditCard;
    }

    public PaymentDeclaration getPaymentDeclaration() {
        return paymentDeclaration;
    }

    public void setPaymentDeclaration(PaymentDeclaration paymentDeclaration) {
        this.paymentDeclaration = paymentDeclaration;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentConfirmation)) return false;
        PaymentConfirmation that = (PaymentConfirmation) o;
        return Objects.equals(getPaymentDeclaration(), that.getPaymentDeclaration()) &&
                Objects.equals(getClientId(), that.getClientId()) &&
                Objects.equals(getCreditCard(), that.getCreditCard());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getPaymentDeclaration(), getClientId(), getCreditCard());
    }
}
