package pl.sii.eu.micuenta.model;

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
    private BigDecimal debtAmount;
    private LocalDate repaymentDate;
    private String uuid;
    private String debtName;

    @ManyToOne
    private Debtor debtor;

    @OneToMany(mappedBy = "debt", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Payment> setOfPayments;

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

    public Set<Payment> getSetOfPayments() {
        return Collections.unmodifiableSet(setOfPayments);
    }

    public void setSetOfPayments(Set<Payment> setOfPayments) {
        this.setOfPayments = new HashSet<>(setOfPayments);
    }

    public void addToSetOfPayments(Payment payment) {
        payment.setDebt(this);
        this.setOfPayments.add(payment);
    }

    public Debt() {
    }

    public Debt(BigDecimal debtAmount, LocalDate repaymentDate, Set<Payment> setOfPayments, String uuid, String debtName) {
        this.debtAmount = debtAmount;
        this.repaymentDate = repaymentDate;
        this.uuid = uuid;
        this.debtName = debtName;
        this.setOfPayments = new HashSet<>(setOfPayments);
        for (Payment payment : this.setOfPayments) {
            payment.setDebt(this);
        }
    }

}
