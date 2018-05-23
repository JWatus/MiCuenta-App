package pl.sii.eu.micuenta.jms;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import pl.sii.eu.micuenta.model.Debtor;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

@Component
public class MessageSender {

    private JmsTemplate jmsTemplate;

    public MessageSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

//    public void sendMessage(final Debtor debtor) {
//
//        jmsTemplate.convertAndSend("kolejka", new MessageCreator() {
//            @Override
//            public Message createMessage(Session session) throws JMSException {
//                Message message = session.createObjectMessage(debtor);
//                return message;
//            }
//        });
//    }

}