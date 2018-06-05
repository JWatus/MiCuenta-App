package pl.sii.eu.micuenta.model.model_dto;

import pl.sii.eu.micuenta.model.model_entity.DebtorEntity;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Debtor implements Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private String ssn;
    private Set<Debt> debts;

    public static Debtor convertFromDebtorEntity(DebtorEntity debtorEntity) {

        Debtor debtor = new Debtor();

        debtor.setFirstName(debtorEntity.getFirstName());
        debtor.setLastName(debtorEntity.getLastName());
        debtor.setSsn(debtorEntity.getSsn());

        Set<Debt> debts = new HashSet<>();
        debtorEntity.getDebtEntities().forEach(d -> debts.add(
                Debt.convertFromDebtEntity(d)));
        debtor.setDebts(debts);

        return debtor;
    }

    public Debtor() {
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

    public Set<Debt> getDebts() {
        return Collections.unmodifiableSet(debts);
    }

    public void setDebts(Set<Debt> debts) {
        this.debts = new HashSet<>(debts);
    }

    public void addToSetOfDebts(Debt debt) {
        debt.setDebtor(this);
        this.debts.add(debt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Debtor)) return false;
        Debtor debtor = (Debtor) o;
        return Objects.equals(getFirstName(), debtor.getFirstName()) &&
                Objects.equals(getLastName(), debtor.getLastName()) &&
                Objects.equals(getSsn(), debtor.getSsn()) &&
                Objects.equals(getDebts(), debtor.getDebts());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getFirstName(), getLastName(), getSsn(), getDebts());
    }
}
