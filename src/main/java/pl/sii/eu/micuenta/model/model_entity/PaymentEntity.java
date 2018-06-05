package pl.sii.eu.micuenta.model.model_entity;

import org.springframework.format.annotation.DateTimeFormat;
import pl.sii.eu.micuenta.model.model_dto.Payment;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class PaymentEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate paymentDate;
    private BigDecimal paymentAmount;
    private String clientId;
    @OneToOne(cascade = CascadeType.ALL)
    private CreditCardEntity creditCardEntity;
    @ManyToOne
    private DebtEntity debtEntity;

    public static PaymentEntity convertFromPayment(Payment payment) {

        PaymentEntity paymentEntity = new PaymentEntity();

        paymentEntity.setId(payment.getId());
        paymentEntity.setClientId(payment.getClientId());
        paymentEntity.setPaymentAmount(payment.getPaymentAmount());
        paymentEntity.setPaymentDate(payment.getPaymentDate());

        paymentEntity.setCreditCardEntity(
                CreditCardEntity.convertFromCreditCard(payment.getCreditCard()));

        return paymentEntity;
    }

    public PaymentEntity() {
    }

    public PaymentEntity(LocalDate paymentDate, BigDecimal paymentAmount, CreditCardEntity creditCardEntity, String clientId) {
        this.paymentDate = paymentDate;
        this.paymentAmount = paymentAmount;
        this.clientId = clientId;
        this.creditCardEntity = creditCardEntity;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public String getClientId() {
        return clientId;
    }

    public CreditCardEntity getCreditCardEntity() {
        return creditCardEntity;
    }

    public DebtEntity getDebtEntity() {
        return debtEntity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setCreditCardEntity(CreditCardEntity creditCardEntity) {
        this.creditCardEntity = creditCardEntity;
    }

    public void setDebtEntity(DebtEntity debtEntity) {
        this.debtEntity = debtEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentEntity)) return false;
        PaymentEntity that = (PaymentEntity) o;
        return Objects.equals(getPaymentDate(), that.getPaymentDate()) &&
                Objects.equals(getPaymentAmount(), that.getPaymentAmount()) &&
                Objects.equals(getClientId(), that.getClientId()) &&
                Objects.equals(getCreditCardEntity(), that.getCreditCardEntity());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getPaymentDate(), getPaymentAmount(), getClientId(), getCreditCardEntity());
    }
}
