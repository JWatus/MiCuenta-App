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
public class MessageDebtorReceiver {

    private MessageSender messageSender;
    private DataDebtorService dataDebtorService;
    private ObjectMapper objectMapper;

    public MessageDebtorReceiver(DataDebtorService dataDebtorService, MessageSender sender, ObjectMapper objectMapper) {
        this.dataDebtorService = dataDebtorService;
        this.messageSender = sender;
        this.objectMapper = objectMapper;
    }

    @JmsListener(destination = "jms.queue.login")
    public void consume(TextMessage textMessage) throws JMSException, IOException {
        String json = textMessage.getText();
        Debtor debtor = objectMapper.readValue(json, Debtor.class);
        String status = dataDebtorService.validateDebtorsData(debtor).toString();
        String queue = "jms.queue." + textMessage.getStringProperty("client").toLowerCase();
        messageSender.send(queue, status, "login");
    }
}
