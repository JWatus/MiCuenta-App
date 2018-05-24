package pl.sii.eu.micuenta.jms.receiver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pl.sii.eu.micuenta.jms.sender.MessageSender;
import pl.sii.eu.micuenta.model.form.PaymentDeclaration;
import pl.sii.eu.micuenta.model.form.PaymentPlan;
import pl.sii.eu.micuenta.service.PaymentPlanService;

import javax.jms.JMSException;
import javax.jms.TextMessage;

@Component
public class MessagePaymentDeclarationReceiver {

    private MessageSender messageSender;
    private PaymentPlanService paymentPlanService;

    public MessagePaymentDeclarationReceiver(PaymentPlanService paymentPlanService, MessageSender sender) {
        this.paymentPlanService = paymentPlanService;
        this.messageSender = sender;
    }

    @JmsListener(destination = "jms.queue.paymentplan")
    public void consume(TextMessage textMessage) throws JMSException, JsonProcessingException {
        String json = textMessage.getText();
        Gson gson = new GsonBuilder().create();
        PaymentDeclaration paymentDeclaration = gson.fromJson(json, PaymentDeclaration.class);
        PaymentPlan paymentPlan = paymentPlanService.getPaymentPlanBasedOnPaymentDeclaration(paymentDeclaration);
        String queue = "jms.queue.paymentplan." + textMessage.getJMSCorrelationID().toLowerCase();
        messageSender.send(queue, paymentPlan);
    }
}
