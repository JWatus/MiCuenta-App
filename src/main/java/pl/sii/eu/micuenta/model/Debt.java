package pl.sii.eu.micuenta.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Debt implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ApiModelProperty(access = "private", name = "debtAmount", example = "50000.0", value = "Amount of Debt")
    private BigDecimal debtAmount;
    @ApiModelProperty(access = "private", name = "repaymentDate", example = "2018-05-08", value = "Date of repayment")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate repaymentDate;
    @ApiModelProperty(access = "private", name = "uuid", example = "999888777666", value = "Universal unique identifier")
    private String uuid;
    @ApiModelProperty(access = "private", name = "debtName", example = "fastLoan", value = "Name of the loan")
    private String debtName;

    @ApiModelProperty(access = "private", name = "debtor", example = "{}", value = "Debtor")
    @ManyToOne
    @JsonIgnore
    private Debtor debtor;

    @ApiModelProperty(access = "private", name = "payments", dataType = "Set", value = "Debtor's set of payments")
    @OneToMany(mappedBy = "debt", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Payment> payments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getDebtAmount() {
        return debtAmount;
    }

    public void setDebtAmount(BigDecimal debtAmount) {
        this.debtAmount = debtAmount;
    }

    public LocalDate getRepaymentDate() {
        return repaymentDate;
    }

    public void setRepaymentDate(LocalDate repaymentDate) {
        this.repaymentDate = repaymentDate;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDebtName() {
        return debtName;
    }

    public void setDebtName(String debtName) {
        this.debtName = debtName;
    }

    public Debtor getDebtor() {
        return debtor;
    }

    public void setDebtor(Debtor debtor) {
        this.debtor = debtor;
    }

    public Set<Payment> getPayments() {
        return Collections.unmodifiableSet(payments);
    }

    public void setPayments(Set<Payment> payments) {
        this.payments = new HashSet<>(payments);
    }

    public void addToSetOfPayments(Payment payment) {
        payment.setDebt(this);
        this.payments.add(payment);
    }

    public Debt() {
    }

    public Debt(BigDecimal debtAmount, LocalDate repaymentDate, Set<Payment> payments, String uuid, String debtName) {
        this.debtAmount = debtAmount;
        this.repaymentDate = repaymentDate;
        this.uuid = uuid;
        this.debtName = debtName;
        this.payments = new HashSet<>(payments);
        for (Payment payment : this.payments) {
            payment.setDebt(this);
        }
    }

}
