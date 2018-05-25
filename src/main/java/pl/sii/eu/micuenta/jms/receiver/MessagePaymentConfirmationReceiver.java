package pl.sii.eu.micuenta.jms.receiver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pl.sii.eu.micuenta.jms.sender.MessageSender;
import pl.sii.eu.micuenta.model.form.PaymentConfirmation;
import pl.sii.eu.micuenta.service.UpdatePaymentService;

import javax.jms.JMSException;
import javax.jms.TextMessage;

@Component
public class MessagePaymentConfirmationReceiver {

    private MessageSender messageSender;
    private UpdatePaymentService updatePaymentService;

    public MessagePaymentConfirmationReceiver(UpdatePaymentService updatePaymentService, MessageSender sender) {
        this.updatePaymentService = updatePaymentService;
        this.messageSender = sender;
    }

    @JmsListener(destination = "jms.queue.paymentsupdate")
    public void consume(TextMessage textMessage) throws JMSException, JsonProcessingException {
        String json = textMessage.getText();
        Gson gson = new GsonBuilder().create();
        PaymentConfirmation paymentConfirmation = gson.fromJson(json, PaymentConfirmation.class);
        String status = updatePaymentService.updateDebtsPaymentsBasedOnPaymentConfirmation(paymentConfirmation).toString();
        String queue = "jms.queue." + textMessage.getJMSCorrelationID().toLowerCase();
        messageSender.send(queue, status, "paymentsupdate");
    }
}
