package pl.sii.eu.micuenta.jms.receiver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pl.sii.eu.micuenta.jms.sender.MessageSender;
import pl.sii.eu.micuenta.model.model_dto.Debtor;
import pl.sii.eu.micuenta.service.DataDebtorService;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.io.IOException;

@Component
public class MessageSsnReceiver {

    private MessageSender messageSender;
    private DataDebtorService dataDebtorService;
    private ObjectMapper objectMapper;

    public MessageSsnReceiver(DataDebtorService dataDebtorService, MessageSender sender, ObjectMapper objectMapper) {
        this.dataDebtorService = dataDebtorService;
        this.messageSender = sender;
        this.objectMapper = objectMapper;
    }

    @JmsListener(destination = "jms.queue.balance")
    public void consume(TextMessage textMessage) throws JMSException, IOException {
        String json = textMessage.getText();
        String ssn = objectMapper.readValue(json, String.class);
        Debtor debtor = dataDebtorService.getDebtorBySsn(ssn);
        String queue = "jms.queue." + textMessage.getStringProperty("client").toLowerCase();
        String message = objectMapper.writeValueAsString(debtor);
        messageSender.send(queue, message, "balance");
    }
}