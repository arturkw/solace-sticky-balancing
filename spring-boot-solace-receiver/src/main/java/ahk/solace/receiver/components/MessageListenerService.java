package ahk.solace.receiver.components;

import ahk.solace.receiver.model.Queue;
import ahk.solace.receiver.jms.SolaceMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class MessageListenerService {

    @Value("${message.listener.duration.sec}")
    private int messageListenerDuration;

    @Autowired
    private Map<Queue, DefaultMessageListenerContainer> listenerContainerMap;

    public void stopMessageListener(Queue queue) {
        DefaultMessageListenerContainer listener = listenerContainerMap.get(queue);
        listener.shutdown();
        listener.setMessageListener(null);
    }

    public void startMessageListener(Queue queue) {
        DefaultMessageListenerContainer listener = listenerContainerMap.get(queue);
        listener.setMessageListener(new SolaceMessageListener());
        listener.afterPropertiesSet();
        listener.start();
    }

    public Set<Queue> findQueuesWithActiveListener() {
        Set<Queue> queues = new HashSet<>();
        listenerContainerMap.forEach((k, v) -> {
            if (v.getMessageListener() != null) {
                if (v.getMessageListener() instanceof SolaceMessageListener) {
                    LocalDateTime lastMessageReceivedAt = ((SolaceMessageListener) v.getMessageListener()).getLastMessageReceivedAt();
                    if (isQueueActive(lastMessageReceivedAt)) {
                        queues.add(k);
                    }
                }
            }
        });
        return queues;
    }

    private boolean isQueueActive(LocalDateTime lastMessageReceivedAt) {
        return LocalDateTime.now().minusSeconds(messageListenerDuration).isBefore(lastMessageReceivedAt);
    }

}
