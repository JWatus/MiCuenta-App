package pl.sii.eu.micuenta.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;
import pl.sii.eu.micuenta.model.Debtor;
import pl.sii.eu.micuenta.service.controller.DataDebtorService;

import javax.jms.JMSException;
import javax.jms.Message;

@Component
public class MessageDebtorReceiver {

    private JmsTemplate jmsTemplate;
    private MessageConverter messageConverter;
    private DataDebtorService dataDebtorService;

    public MessageDebtorReceiver(JmsTemplate jmsTemplate, MessageConverter messageConverter) {
        this.jmsTemplate = jmsTemplate;
        this.messageConverter = messageConverter;
    }

    @JmsListener(destination = "jms.queue.login")
    public void receiveDebtorAndReturnStatus(Message message) throws JMSException {
        Debtor debtor = (Debtor) messageConverter.fromMessage(message);
        String status = dataDebtorService.validateDebtorsData(debtor).toString();
    }
}
