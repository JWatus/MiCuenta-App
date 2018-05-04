package pl.sii.eu.micuenta.model;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Debtor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String ssn;

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
