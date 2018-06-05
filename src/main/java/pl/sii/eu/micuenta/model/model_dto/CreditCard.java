package pl.sii.eu.micuenta.model.model_dto;

import org.springframework.format.annotation.DateTimeFormat;
import pl.sii.eu.micuenta.model.model_entity.CreditCardEntity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class CreditCard implements Serializable {

    private Long id;
    private String ccNumber;
    private String cvv;
    private String firstName;
    private String lastName;
    private String issuingNetwork;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expDate;

    public static CreditCard convertFromCreditCardEntity(CreditCardEntity creditCardEntity) {

        CreditCard creditCard = new CreditCard();

        creditCard.setId(creditCardEntity.getId());
        creditCard.setCcNumber(creditCardEntity.getCcNumber());
        creditCard.setCvv(creditCardEntity.getCvv());
        creditCard.setFirstName(creditCardEntity.getFirstName());
        creditCard.setLastName(creditCardEntity.getLastName());
        creditCard.setIssuingNetwork(creditCardEntity.getIssuingNetwork());
        creditCard.setExpDate(creditCardEntity.getExpDate());

        return creditCard;
    }

    public CreditCard() {
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
        if (!(o instanceof CreditCard)) return false;
        CreditCard that = (CreditCard) o;
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
