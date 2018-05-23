package pl.sii.eu.micuenta.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;
import pl.sii.eu.micuenta.model.form.PaymentDeclaration;
import pl.sii.eu.micuenta.model.form.PaymentPlan;
import pl.sii.eu.micuenta.service.controller.PaymentPlanService;

import javax.jms.JMSException;
import javax.jms.Message;

@Component
public class MessagePaymentDeclarationReceiver {

    private JmsTemplate jmsTemplate;
    private MessageConverter messageConverter;
    private PaymentPlanService paymentPlanService;

    public MessagePaymentDeclarationReceiver(JmsTemplate jmsTemplate, MessageConverter messageConverter, PaymentPlanService paymentPlanService) {
        this.jmsTemplate = jmsTemplate;
        this.messageConverter = messageConverter;
        this.paymentPlanService = paymentPlanService;
    }

    @JmsListener(destination = "jms.queue.paymentplan")
    public void receivePaymentDeclaration(Message message) throws JMSException {
        PaymentDeclaration declaration = (PaymentDeclaration) messageConverter.fromMessage(message);
        PaymentPlan paymentPlan = paymentPlanService.getPaymentPlanBasedOnPaymentDeclaration(declaration);
    }
}



