package pl.sii.eu.micuenta.jms.receiver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pl.sii.eu.micuenta.jms.sender.MessageSender;
import pl.sii.eu.micuenta.model.form.PaymentConfirmation;
import pl.sii.eu.micuenta.service.UpdatePaymentService;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.io.IOException;

@Component
public class MessagePaymentConfirmationReceiver {

    private MessageSender messageSender;
    private UpdatePaymentService updatePaymentService;
    private ObjectMapper objectMapper;

    public MessagePaymentConfirmationReceiver(UpdatePaymentService updatePaymentService, MessageSender sender, ObjectMapper objectMapper) {
        this.updatePaymentService = updatePaymentService;
        this.messageSender = sender;
        this.objectMapper = objectMapper;
    }

    @JmsListener(destination = "jms.queue.paymentsupdate")
    public void consume(TextMessage textMessage) throws JMSException, IOException {
        String json = textMessage.getText();
        PaymentConfirmation paymentConfirmation = objectMapper.readValue(json, PaymentConfirmation.class);
        String status = updatePaymentService.updateDebtsPaymentsBasedOnPaymentConfirmation(paymentConfirmation).toString();
        String queue = "jms.queue." + textMessage.getJMSCorrelationID().toLowerCase();
        messageSender.send(queue, status, "paymentsupdate");
    }
}
