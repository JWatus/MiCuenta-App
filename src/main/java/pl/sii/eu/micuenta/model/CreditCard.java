package pl.sii.eu.micuenta.model;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
public class CreditCard implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ApiModelProperty(access = "private", name = "ccNumber", example = "98978872537125", value = "Credit Card Number")
    private String ccNumber;
    @ApiModelProperty(access = "private", name = "cvv", example = "109", value = "Credit Card CVV number")
    private String cvv;
    @ApiModelProperty(access = "private", name = "firstName", example = "Jakub", value = "Credit Card owner's first name")
    private String firstName;
    @ApiModelProperty(access = "private", name = "lastName", example = "Watus", value = "Credit Card owner's last name")
    private String lastName;
    @ApiModelProperty(access = "private", name = "issuingNetwork", example = "MasterCard", value = "Credit Card vendor")
    private String issuingNetwork;
    @ApiModelProperty(access = "private", name = "expDate", example = "2018-06-30", value = "Credit Card expiration date")
    private LocalDate expDate;

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

    public CreditCard() {
    }

    public CreditCard(String ccNumber, String cvv, String firstName, String lastName, String issuingNetwork, LocalDate expDate) {
        this.ccNumber = ccNumber;
        this.cvv = cvv;
        this.firstName = firstName;
        this.lastName = lastName;
        this.issuingNetwork = issuingNetwork;
        this.expDate = expDate;
    }

}
