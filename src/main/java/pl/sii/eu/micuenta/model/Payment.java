package pl.sii.eu.micuenta.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Payment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate paymentDate;
    private BigDecimal paymentAmount;

    @OneToOne(cascade = CascadeType.ALL)
    private CreditCard creditCard;

    @ManyToOne
    private Debt debt;

    public Long getId() {
        return id;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public Debt getDebt() {
        return debt;
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

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public void setDebt(Debt debt) {
        this.debt = debt;
    }

    public Payment() {
    }

    public Payment(LocalDate paymentDate, BigDecimal paymentAmount, CreditCard creditCard) {
        this.paymentDate = paymentDate;
        this.paymentAmount = paymentAmount;
        this.creditCard = creditCard;
    }

}
