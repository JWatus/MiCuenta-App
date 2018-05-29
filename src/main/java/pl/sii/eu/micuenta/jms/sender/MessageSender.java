package pl.sii.eu.micuenta.jms.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Session;
import javax.jms.TextMessage;

@Component
public class MessageSender {

    private JmsTemplate jmsTemplate;
    private ObjectMapper objectMapper;

    public MessageSender(JmsTemplate jmsTemplate, ObjectMapper objectMapper) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
    }

    public <T> void send(String queue, T response, String endPoint) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(response);
        jmsTemplate.send(queue, (Session session) -> {
            TextMessage textMessage = new ActiveMQTextMessage();
            textMessage.setText(json);
            textMessage.setStringProperty("endpoint", endPoint);
            textMessage.setObjectProperty("_type", response.getClass().getName());
            return textMessage;
        });
    }
}

