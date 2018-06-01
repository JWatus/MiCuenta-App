package pl.sii.eu.micuenta.model;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "findFirstBySsn",
                query = "select d from Debtor d where d.ssn=:ssn"
        ),
        @NamedQuery(
                name = "findFirstBySsnFirstNameLastName",
                query = "select d from Debtor d where d.ssn =:ssn and d.firstName =:firstName and d.lastName =:lastName"
        )
})

public class Debtor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ApiModelProperty(access = "private", name = "firstName", example = "Jakub", value = "Debtor's first name")
    private String firstName;
    @ApiModelProperty(access = "private", name = "lastName", example = "Watus", value = "Debtor's last name")
    private String lastName;
    @ApiModelProperty(access = "private", name = "ssn", example = "980-122-111", value = "Debtor's social security number")
    @Column(unique = true)
    private String ssn;

    @ApiModelProperty(access = "private", name = "debts", dataType = "Set", value = "Debtor's set of debts")
    @OneToMany(mappedBy = "debtor", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Debt> debts;

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

    public Debtor() {
    }

    public Debtor(String firstName, String lastName, String ssn, Set<Debt> debts) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ssn = ssn;
        this.debts = new HashSet<>(debts);
        for (Debt debt : this.debts) {
            debt.setDebtor(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Debtor debtor = (Debtor) o;
        return Objects.equals(firstName, debtor.firstName) &&
                Objects.equals(lastName, debtor.lastName) &&
                Objects.equals(ssn, debtor.ssn) &&
                Objects.equals(debts, debtor.debts);
    }

    @Override
    public int hashCode() {

        return Objects.hash(firstName, lastName, ssn, debts);
    }
}
