package pl.sii.eu.micuenta.jms.receiver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pl.sii.eu.micuenta.jms.sender.MessageSender;
import pl.sii.eu.micuenta.model.Debtor;
import pl.sii.eu.micuenta.service.DataDebtorService;

import javax.jms.JMSException;
import javax.jms.TextMessage;

@Component
public class MessageDebtorReceiver {

    private MessageSender messageSender;
    private DataDebtorService dataDebtorService;

    public MessageDebtorReceiver(DataDebtorService dataDebtorService, MessageSender sender) {
        this.dataDebtorService = dataDebtorService;
        this.messageSender = sender;
    }

    @JmsListener(destination = "jms.queue.login")
    public void consume(TextMessage textMessage) throws JMSException, JsonProcessingException {
        String json = textMessage.getText();
        Gson gson = new GsonBuilder().create();
        Debtor debtor = gson.fromJson(json, Debtor.class);
        String status = dataDebtorService.validateDebtorsData(debtor).toString();
        String queue = "jms.queue.login." + textMessage.getJMSCorrelationID().toLowerCase();
        messageSender.send(queue, status);
    }
}
