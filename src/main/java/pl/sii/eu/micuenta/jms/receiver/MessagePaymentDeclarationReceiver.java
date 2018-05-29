package pl.sii.eu.micuenta.jms.receiver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pl.sii.eu.micuenta.jms.sender.MessageSender;
import pl.sii.eu.micuenta.model.form.PaymentDeclaration;
import pl.sii.eu.micuenta.model.form.PaymentPlan;
import pl.sii.eu.micuenta.service.PaymentPlanService;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.io.IOException;

@Component
public class MessagePaymentDeclarationReceiver {

    private MessageSender messageSender;
    private PaymentPlanService paymentPlanService;
    private ObjectMapper objectMapper;

    public MessagePaymentDeclarationReceiver(PaymentPlanService paymentPlanService, MessageSender sender, ObjectMapper objectMapper) {
        this.paymentPlanService = paymentPlanService;
        this.messageSender = sender;
        this.objectMapper = objectMapper;
    }

    @JmsListener(destination = "jms.queue.paymentplan")
    public void consume(TextMessage textMessage) throws JMSException, IOException {
        String json = textMessage.getText();
        PaymentDeclaration paymentDeclaration = objectMapper.readValue(json, PaymentDeclaration.class);
        PaymentPlan paymentPlan = paymentPlanService.getPaymentPlanBasedOnPaymentDeclaration(paymentDeclaration);
        String queue = "jms.queue." + textMessage.getJMSCorrelationID().toLowerCase();
        messageSender.send(queue, paymentPlan, "paymentplan");
    }
}
