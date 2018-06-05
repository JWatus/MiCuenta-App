package pl.sii.eu.micuenta.model.model_entity;

import org.springframework.format.annotation.DateTimeFormat;
import pl.sii.eu.micuenta.model.model_dto.Debt;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class DebtEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal debtAmount;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate repaymentDate;
    private String uuid;
    private String debtName;
    @ManyToOne
    private DebtorEntity debtorEntity;
    @OneToMany(mappedBy = "debtEntity", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<PaymentEntity> paymentEntities;

    public static DebtEntity convertFromDebt(Debt debt) {

        DebtEntity debtEntity = new DebtEntity();

        debtEntity.setDebtAmount(debt.getDebtAmount());
        debtEntity.setDebtName(debt.getDebtName());
        debtEntity.setRepaymentDate(debt.getRepaymentDate());
        debtEntity.setUuid(debt.getUuid());

        Set<PaymentEntity> paymentEntities = new HashSet<>();
        debt.getPayments().forEach(p -> paymentEntities.add(
                PaymentEntity.convertFromPayment(p)));
        debtEntity.setPaymentEntities(paymentEntities);

        return debtEntity;
    }

    public DebtEntity() {
    }

    public DebtEntity(BigDecimal debtAmount, LocalDate repaymentDate, Set<PaymentEntity> paymentEntities, String uuid, String debtName) {
        this.debtAmount = debtAmount;
        this.repaymentDate = repaymentDate;
        this.uuid = uuid;
        this.debtName = debtName;
        this.paymentEntities = new HashSet<>(paymentEntities);
        for (PaymentEntity paymentEntity : this.paymentEntities) {
            paymentEntity.setDebtEntity(this);
        }
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

    public DebtorEntity getDebtorEntity() {
        return debtorEntity;
    }

    public void setDebtorEntity(DebtorEntity debtorEntity) {
        this.debtorEntity = debtorEntity;
    }

    public Set<PaymentEntity> getPaymentEntities() {
        return Collections.unmodifiableSet(paymentEntities);
    }

    public void setPaymentEntities(Set<PaymentEntity> paymentEntities) {
        this.paymentEntities = new HashSet<>(paymentEntities);
    }

    public void addToSetOfPaymentEntities(PaymentEntity paymentEntity) {
        paymentEntity.setDebtEntity(this);
        this.paymentEntities.add(paymentEntity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DebtEntity)) return false;
        DebtEntity that = (DebtEntity) o;
        return Objects.equals(getDebtAmount(), that.getDebtAmount()) &&
                Objects.equals(getRepaymentDate(), that.getRepaymentDate()) &&
                Objects.equals(getUuid(), that.getUuid()) &&
                Objects.equals(getDebtName(), that.getDebtName()) &&
                Objects.equals(getPaymentEntities(), that.getPaymentEntities());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getDebtAmount(), getRepaymentDate(), getUuid(), getDebtName(), getPaymentEntities());
    }
}
