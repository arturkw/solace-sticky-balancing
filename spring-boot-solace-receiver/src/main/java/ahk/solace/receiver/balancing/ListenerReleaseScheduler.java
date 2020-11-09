package ahk.solace.receiver.balancing;

import ahk.solace.receiver.model.InstanceInfo;
import ahk.solace.receiver.model.Queue;
import ahk.solace.receiver.components.MessageListenerService;
import ahk.solace.receiver.model.BalancingMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.Topic;
import java.util.Set;

@Component
public class ListenerReleaseScheduler {

    @Autowired
    private MessageListenerService messageListenerService;

    @Autowired
    private JmsTemplate topicJmsTemplate;

    @Autowired
    private InstanceInfo instanceInfo;

    @Autowired
    private Topic balancingTopic;

    @Scheduled(cron = "${cron.balancing.expression}")
    void sendBalancingMessage() throws Exception {
        Set<Queue> activeQueues = messageListenerService.findQueuesWithActiveListener();
        BalancingMessage balancingMessage = new BalancingMessage();
        balancingMessage.setInstanceId(instanceInfo.getId());
        balancingMessage.setNumOfQueuesConnected(activeQueues.size());
        topicJmsTemplate.convertAndSend(balancingTopic, balancingMessage);
    }

}
