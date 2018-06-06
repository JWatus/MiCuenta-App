package pl.sii.eu.micuenta.model.model_dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;
import pl.sii.eu.micuenta.model.model_entity.DebtEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Debt implements Serializable {

    private Long id;
    private BigDecimal debtAmount;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate repaymentDate;
    private String uuid;
    private String debtName;
    @JsonIgnore
    private Debtor debtor;
    private Set<Payment> payments = Collections.emptySet();;

    public static Debt convertFromDebtEntity(DebtEntity debtEntity) {

        Debt debt = new Debt();

        debt.setId(debtEntity.getId());
        debt.setDebtAmount(debtEntity.getDebtAmount());
        debt.setDebtName(debtEntity.getDebtName());
        debt.setRepaymentDate(debtEntity.getRepaymentDate());
        debt.setUuid(debtEntity.getUuid());

        Set<Payment> payments = new HashSet<>();
        debtEntity.getPaymentEntities().forEach(p -> payments.add(
                Payment.convertFromPaymentEntity(p)));
        debt.setPayments(payments);

        return debt;
    }

    public Debt() {
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Debt)) return false;
        Debt debt = (Debt) o;
        return Objects.equals(getDebtAmount(), debt.getDebtAmount()) &&
                Objects.equals(getRepaymentDate(), debt.getRepaymentDate()) &&
                Objects.equals(getUuid(), debt.getUuid()) &&
                Objects.equals(getDebtName(), debt.getDebtName()) &&
                Objects.equals(payments, debt.payments);
    }

    @Override
    public int hashCode() {

        return Objects.hash(getDebtAmount(), getRepaymentDate(), getUuid(), getDebtName(), payments);
    }
}
