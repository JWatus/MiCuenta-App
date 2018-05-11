package pl.sii.eu.micuenta.model.form;

import pl.sii.eu.micuenta.model.CreditCard;

public class PaymentConfirmation {

    private PaymentDeclaration paymentDeclaration;
    private String clientId;
    private CreditCard creditCard;

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

    public PaymentConfirmation() {
    }

    public PaymentConfirmation(PaymentDeclaration paymentDeclaration, String clientId, CreditCard creditCard) {
        this.paymentDeclaration = paymentDeclaration;
        this.clientId = clientId;
        this.creditCard = creditCard;
    }
}
