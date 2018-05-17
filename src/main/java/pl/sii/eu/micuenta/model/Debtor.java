package pl.sii.eu.micuenta.model;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Debtor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ApiModelProperty(access = "private", name = "firstName", example = "Jakub", value = "Debtor's first name")
    private String firstName;
    @ApiModelProperty(access = "private", name = "lastName", example = "Watus", value = "Debtor's last name")
    private String lastName;
    @ApiModelProperty(access = "private", name = "ssn", example = "980-122-111", value = "Debtor's social security number")
    private String ssn;

    @ApiModelProperty(access = "private", name = "setOfDebts", dataType = "Set", value = "Debtor's set of debts")
    @OneToMany(mappedBy = "debtor", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Debt> setOfDebts;

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

    public Set<Debt> getSetOfDebts() {
        return Collections.unmodifiableSet(setOfDebts);
    }

    public void setSetOfDebts(Set<Debt> setOfDebts) {
        this.setOfDebts = new HashSet<>(setOfDebts);
    }

    public void addToSetOfDebts(Debt debt) {
        debt.setDebtor(this);
        this.setOfDebts.add(debt);
    }

    public Debtor() {
    }

    public Debtor(String firstName, String lastName, String ssn, Set<Debt> setOfDebts) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ssn = ssn;
        this.setOfDebts = new HashSet<>(setOfDebts);
        for (Debt debt : this.setOfDebts) {
            debt.setDebtor(this);
        }
    }

}
