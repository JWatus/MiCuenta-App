package pl.sii.eu.micuenta.jms.receiver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pl.sii.eu.micuenta.jms.sender.MessageSender;
import pl.sii.eu.micuenta.service.DataDebtorService;

import javax.jms.JMSException;
import javax.jms.TextMessage;

@Component
public class MessageSsnReceiver {

    private MessageSender messageSender;
    private DataDebtorService dataDebtorService;

    public MessageSsnReceiver(DataDebtorService dataDebtorService, MessageSender sender) {
        this.dataDebtorService = dataDebtorService;
        this.messageSender = sender;
    }

    @JmsListener(destination = "jms.queue.balance")
    public void consume(TextMessage textMessage) throws JMSException, JsonProcessingException {
        String json = textMessage.getText();
        Gson gson = new GsonBuilder().create();
        String ssn = gson.fromJson(json, String.class);
        String debtor = dataDebtorService.getDebtorBySsn(ssn);
        String queue = "jms.queue." + textMessage.getJMSCorrelationID().toLowerCase();
        messageSender.sendDebtor(queue, debtor,"balance");
    }
}
