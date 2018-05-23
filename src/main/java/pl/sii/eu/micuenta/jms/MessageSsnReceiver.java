package pl.sii.eu.micuenta.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;
import pl.sii.eu.micuenta.model.Debtor;
import pl.sii.eu.micuenta.service.controller.DataDebtorService;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

@Component
public class MessageSsnReceiver {

    private DataDebtorService dataDebtorService;
    private JmsTemplate jmsTemplate;
    private MessageConverter messageConverter;

    public MessageSsnReceiver(DataDebtorService dataDebtorService, JmsTemplate jmsTemplate, MessageConverter messageConverter) {
        this.dataDebtorService = dataDebtorService;
        this.jmsTemplate = jmsTemplate;
        this.messageConverter = messageConverter;
    }

    @JmsListener(destination = "jms.queue.balance")
    public void receiveSsnAndReturnDebtor(Message message) throws JMSException {
        String ssn = (String) messageConverter.fromMessage(message);
        Debtor debtor = dataDebtorService.getDebtorBySsn(ssn);
        message.getStringProperty("Alivio");
/*        jmsTemplate.convertAndSend("jms.queue.alivio", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                Message message = session.createObjectMessage(debtor);
                message.setJMSCorrelationID(message.getJMSCorrelationID());
                //message.setStringProperty("UI_view", "debtor_view_z_UI_przepisane_property");
                return message;
            }
        })*/;
    }
}



