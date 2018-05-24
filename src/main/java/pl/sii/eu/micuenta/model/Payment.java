package pl.sii.eu.micuenta.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Payment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ApiModelProperty(access = "private", name = "paymentDate", example = "2018-05-08", value = "Date of payment")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate paymentDate;
    @ApiModelProperty(access = "private", name = "paymentAmount", example = "50000.0", value = "Amount of Payment")
    private BigDecimal paymentAmount;
    @ApiModelProperty(access = "private", name = "clientId", example = "Alivio", value = "Client's name")
    private String clientId;

    @ApiModelProperty(access = "private", name = "creditCard", example = "{}", value = "Payment from this Credit Card")
    @OneToOne(cascade = CascadeType.ALL)
    private CreditCard creditCard;

    @ApiModelProperty(access = "private", name = "debt", example = "{}", value = "Debt")
    @ManyToOne
    @JsonIgnore
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

    public String getClientId() {
        return clientId;
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

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public void setDebt(Debt debt) {
        this.debt = debt;
    }

    public Payment() {
    }

    public Payment(LocalDate paymentDate, BigDecimal paymentAmount, CreditCard creditCard, String clientId) {
        this.paymentDate = paymentDate;
        this.paymentAmount = paymentAmount;
        this.clientId = clientId;
        this.creditCard = creditCard;
    }

}
