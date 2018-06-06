package pl.sii.eu.micuenta.model.model_entity;

import pl.sii.eu.micuenta.model.model_dto.Debtor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NamedQueries({
        @NamedQuery(
                name = "findFirstBySsn",
                query = "select d from DebtorEntity d where d.ssn=:ssn"
        ),
        @NamedQuery(
                name = "findFirstBySsnFirstNameLastName",
                query = "select d from DebtorEntity d where d.ssn =:ssn and d.firstName =:firstName and d.lastName =:lastName"
        )
})
@Entity
public class DebtorEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String ssn;
    @OneToMany(mappedBy = "debtorEntity", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<DebtEntity> debtEntities = Collections.emptySet();

    public static DebtorEntity convertFromDebtor(Debtor debtor) {

        DebtorEntity debtorEntity = new DebtorEntity();

        debtorEntity.setFirstName(debtor.getFirstName());
        debtorEntity.setLastName(debtor.getLastName());
        debtorEntity.setSsn(debtor.getSsn());

        Set<DebtEntity> debtEntities = new HashSet<>();
        debtor.getDebts().forEach(d -> debtEntities.add(
                DebtEntity.convertFromDebt(d)));
        debtorEntity.setDebtEntities(debtEntities);

        return debtorEntity;
    }

    public DebtorEntity() {
    }

    public DebtorEntity(String firstName, String lastName, String ssn, Set<DebtEntity> debtEntities) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ssn = ssn;
        this.debtEntities = new HashSet<>(debtEntities);
        for (DebtEntity debtEntity : this.debtEntities) {
            debtEntity.setDebtorEntity(this);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public Set<DebtEntity> getDebtEntities() {
        return Collections.unmodifiableSet(debtEntities);
    }

    public void setDebtEntities(Set<DebtEntity> debtEntities) {
        this.debtEntities = new HashSet<>(debtEntities);
    }

    public void addToSetOfDebtEntities(DebtEntity debtEntity) {
        debtEntity.setDebtorEntity(this);
        this.debtEntities.add(debtEntity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DebtorEntity)) return false;
        DebtorEntity that = (DebtorEntity) o;
        return Objects.equals(getFirstName(), that.getFirstName()) &&
                Objects.equals(getLastName(), that.getLastName()) &&
                Objects.equals(getSsn(), that.getSsn()) &&
                Objects.equals(getDebtEntities(), that.getDebtEntities());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getFirstName(), getLastName(), getSsn(), getDebtEntities());
    }
}
