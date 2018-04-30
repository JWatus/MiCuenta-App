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

    public Debt(BigDecimal debtAmount, LocalDate repaymentDate, Set<Payment> setOfPayments) {
        this.debtAmount = debtAmount;
        this.repaymentDate = repaymentDate;
        this.setOfPayments = new HashSet<>(setOfPayments);
        for (Payment payment : this.setOfPayments) {
            payment.setDebt(this);
        }
    }

}
