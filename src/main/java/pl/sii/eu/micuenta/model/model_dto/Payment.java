package pl.sii.eu.micuenta.model.model_dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;
import pl.sii.eu.micuenta.model.model_entity.PaymentEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Payment implements Serializable {

    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate paymentDate;
    private BigDecimal paymentAmount;
    private String clientId;
    private CreditCard creditCard;
    @JsonIgnore
    private Debt debt;

    public static Payment convertFromPaymentEntity(PaymentEntity paymentEntity) {

        Payment payment = new Payment();

        payment.setId(paymentEntity.getId());
        payment.setClientId(paymentEntity.getClientId());
        payment.setPaymentAmount(paymentEntity.getPaymentAmount());
        payment.setPaymentDate(paymentEntity.getPaymentDate());

        payment.setCreditCard(
                CreditCard.convertFromCreditCardEntity(paymentEntity.getCreditCardEntity()));

        return payment;
    }

    public Payment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
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

    public Debt getDebt() {
        return debt;
    }

    public void setDebt(Debt debt) {
        this.debt = debt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment)) return false;
        Payment payment = (Payment) o;
        return Objects.equals(getPaymentDate(), payment.getPaymentDate()) &&
                Objects.equals(getPaymentAmount(), payment.getPaymentAmount()) &&
                Objects.equals(getClientId(), payment.getClientId()) &&
                Objects.equals(getCreditCard(), payment.getCreditCard());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getPaymentDate(), getPaymentAmount(), getClientId(), getCreditCard());
    }
}
