package pl.sii.eu.micuenta.jms.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.TextMessage;

@Component
public class MessageSender {

    private JmsTemplate jmsTemplate;

    public MessageSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public <T> void send(String queue, T response) throws JsonProcessingException, JMSException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(response);
        TextMessage activeMQTextMessage = new ActiveMQTextMessage();
        activeMQTextMessage.setText(json);
        jmsTemplate.convertAndSend(queue, activeMQTextMessage);
    }
}

