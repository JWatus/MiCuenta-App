package pl.sii.eu.micuenta.model.model_swagger;

import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;

class CreditCard {

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
}
