package pl.sii.eu.micuenta.model.model_swagger;

import io.swagger.annotations.ApiModelProperty;

import java.util.Set;

class Debtor {

    private Long id;
    @ApiModelProperty(access = "private", name = "firstName", example = "Jakub", value = "Debtor's first name")
    private String firstName;
    @ApiModelProperty(access = "private", name = "lastName", example = "Watus", value = "Debtor's last name")
    private String lastName;
    @ApiModelProperty(access = "private", name = "ssn", example = "980-122-111", value = "Debtor's social security number")
    private String ssn;
    @ApiModelProperty(access = "private", name = "debtEntities", dataType = "Set", value = "Debtor's set of debtEntities")
    private Set<Debt> debtEntities;
}
