package pl.sii.eu.micuenta.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pl.sii.eu.micuenta.model.Debtor;
import pl.sii.eu.micuenta.model.form.PaymentDeclaration;
import pl.sii.eu.micuenta.model.form.PaymentPlan;
import pl.sii.eu.micuenta.service.controller.DataDebtorService;
import pl.sii.eu.micuenta.service.controller.PaymentPlanService;

@Component
public class MessageReceiver {

    private DataDebtorService dataDebtorService;
    private PaymentPlanService paymentPlanService;

    public MessageReceiver(
            DataDebtorService dataDebtorService,
            PaymentPlanService paymentPlanService) {
        this.dataDebtorService = dataDebtorService;
        this.paymentPlanService = paymentPlanService;
    }

    @JmsListener(destination = "jms.queue.login")
    public Debtor receiveSsnAndReturnDebtor(String ssn) {
        return dataDebtorService.getDebtorBySsn(ssn);
    }

    @JmsListener(destination = "jms.queue.paymentplan")
    public PaymentPlan receivePaymentDeclarationAndReturnPaymentPlan(PaymentDeclaration paymentDeclaration) {
        return paymentPlanService.getPaymentPlanBasedOnPaymentDeclaration(paymentDeclaration);
    }


}