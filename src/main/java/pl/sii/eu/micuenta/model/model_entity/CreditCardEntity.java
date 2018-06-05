package pl.sii.eu.micuenta.model.model_entity;

import org.springframework.format.annotation.DateTimeFormat;
import pl.sii.eu.micuenta.model.model_dto.CreditCard;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class CreditCardEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ccNumber;
    private String cvv;
    private String firstName;
    private String lastName;
    private String issuingNetwork;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expDate;

    public static CreditCardEntity convertFromCreditCard(CreditCard creditCard) {

        CreditCardEntity creditCardEntity = new CreditCardEntity();

        creditCardEntity.setId(creditCard.getId());
        creditCardEntity.setCcNumber(creditCard.getCcNumber());
        creditCardEntity.setCvv(creditCard.getCvv());
        creditCardEntity.setFirstName(creditCard.getFirstName());
        creditCardEntity.setLastName(creditCard.getLastName());
        creditCardEntity.setIssuingNetwork(creditCard.getIssuingNetwork());
        creditCardEntity.setExpDate(creditCard.getExpDate());

        return creditCardEntity;
    }

    public CreditCardEntity() {
    }

    public CreditCardEntity(String ccNumber, String cvv, String firstName, String lastName, String issuingNetwork, LocalDate expDate) {
        this.ccNumber = ccNumber;
        this.cvv = cvv;
        this.firstName = firstName;
        this.lastName = lastName;
        this.issuingNetwork = issuingNetwork;
        this.expDate = expDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCcNumber() {
        return ccNumber;
    }

    public void setCcNumber(String ccNumber) {
        this.ccNumber = ccNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
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

    public String getIssuingNetwork() {
        return issuingNetwork;
    }

    public void setIssuingNetwork(String issuingNetwork) {
        this.issuingNetwork = issuingNetwork;
    }

    public LocalDate getExpDate() {
        return expDate;
    }

    public void setExpDate(LocalDate expDate) {
        this.expDate = expDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreditCardEntity)) return false;
        CreditCardEntity that = (CreditCardEntity) o;
        return Objects.equals(getCcNumber(), that.getCcNumber()) &&
                Objects.equals(getCvv(), that.getCvv()) &&
                Objects.equals(getFirstName(), that.getFirstName()) &&
                Objects.equals(getLastName(), that.getLastName()) &&
                Objects.equals(getIssuingNetwork(), that.getIssuingNetwork()) &&
                Objects.equals(getExpDate(), that.getExpDate());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getCcNumber(), getCvv(), getFirstName(), getLastName(), getIssuingNetwork(), getExpDate());
    }
}
