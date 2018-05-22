package pl.sii.eu.micuenta.jms;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {

    private JmsTemplate jmsTemplate;
    MessageReceiver messageReceiver;

    public MessageSender(JmsTemplate jmsTemplate, MessageReceiver messageReceiver) {
        this.jmsTemplate = jmsTemplate;
        this.messageReceiver = messageReceiver;
    }

    public void send(String destination, String message) {
        jmsTemplate.convertAndSend(destination, message);
    }

}