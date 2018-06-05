package pl.sii.eu.micuenta.jms.sender;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Session;
import javax.jms.TextMessage;

@Component
public class MessageSender {

    private JmsTemplate jmsTemplate;

    public MessageSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void send(String queue, String message, String endPoint) {
        jmsTemplate.send(queue, (Session session) -> {
            TextMessage textMessage = new ActiveMQTextMessage();
            textMessage.setText(message);
            textMessage.setStringProperty("endpoint", endPoint);
            return textMessage;
        });
    }
}

