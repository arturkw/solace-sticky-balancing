package ahk.solace.receiver.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.time.LocalDateTime;

public class SolaceMessageListener implements MessageListener {

    private LocalDateTime lastMessageReceivedAt = LocalDateTime.now();

    public SolaceMessageListener() {
    }

    @Override
    public void onMessage(Message message) {
        lastMessageReceivedAt = LocalDateTime.now();
        if (message instanceof TextMessage) {
            try {
                System.out.println(((TextMessage) message).getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(message);
        }
    }

    public LocalDateTime getLastMessageReceivedAt() {
        return lastMessageReceivedAt;
    }

}
