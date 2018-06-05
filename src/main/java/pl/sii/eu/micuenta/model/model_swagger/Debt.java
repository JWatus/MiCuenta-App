package pl.sii.eu.micuenta.model.model_swagger;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

class Debt {

    private Long id;
    @ApiModelProperty(access = "private", name = "debtAmount", example = "50000.0", value = "Amount of Debt")
    private BigDecimal debtAmount;
    @ApiModelProperty(access = "private", name = "repaymentDate", example = "2018-05-08", value = "Date of repayment")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate repaymentDate;
    @ApiModelProperty(access = "private", name = "uuid", example = "999888777666", value = "Universal unique identifier")
    private String uuid;
    @ApiModelProperty(access = "private", name = "debtName", example = "fastLoan", value = "Name of the loan")
    private String debtName;
    @ApiModelProperty(access = "private", name = "debtor", example = "{}", value = "Debtor")
    private Debtor debtor;
    @ApiModelProperty(access = "private", name = "paymentEntities", dataType = "Set", value = "Debtor's set of paymentEntities")
    private Set<Payment> paymentEntities;
}
