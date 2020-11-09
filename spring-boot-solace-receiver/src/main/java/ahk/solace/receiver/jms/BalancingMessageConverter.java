package ahk.solace.receiver.jms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ahk.solace.receiver.model.BalancingMessage;
import com.solacesystems.jms.message.SolTextMessage;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.io.IOException;

public class BalancingMessageConverter implements MessageConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
        TextMessage tm = new SolTextMessage();
        try {
            tm.setText(objectMapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return tm;
    }

    @Override
    public Object fromMessage(Message message) throws JMSException, MessageConversionException {
        if (message instanceof TextMessage) {
            String messageText = ((TextMessage) message).getText();
            try {
                return objectMapper.readValue(messageText, BalancingMessage.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
