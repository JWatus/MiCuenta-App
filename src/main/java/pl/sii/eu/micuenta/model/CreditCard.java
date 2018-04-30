package pl.sii.eu.micuenta.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class CreditCard implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String CCNumber;
    private String cvv;
    private String firstName;
    private String lastName;

    public Long getId() {
        return id;
    }

    public String getCCNumber() {
        return CCNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCCNumber(String CCNumber) {
        this.CCNumber = CCNumber;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public CreditCard() {
    }

    public CreditCard(String CCNumber, String cvv, String firstName, String lastName) {
        this.CCNumber = CCNumber;
        this.cvv = cvv;
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
