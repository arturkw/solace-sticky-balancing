package ahk.solace.receiver.balancing;

import ahk.solace.receiver.model.InstanceInfo;
import ahk.solace.receiver.model.Queue;
import ahk.solace.receiver.components.MessageListenerService;
import ahk.solace.receiver.model.BalancingMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class SynchronizationTopic {

    @Autowired
    private InstanceInfo instanceInfo;

    @Autowired
    private MessageListenerService messageListenerService;

    @JmsListener(destination = "BALANCING_TOPIC", containerFactory = "jmsContainerFactory")
    public void synchronizationMessage(BalancingMessage message) throws Exception {
        Set<Queue> myActiveConnections = messageListenerService.findQueuesWithActiveListener();

        if (isTriggeredByConcurrentInstance(message)) {
            balanceInstances(message.getNumOfQueuesConnected(), myActiveConnections);
        } else {
            reconnect(myActiveConnections);
        }

    }

    private void reconnect(Set<Queue> myActiveConnections) {
        Set<Queue> allQueues = new HashSet<>(Arrays.asList(Queue.values()));
        allQueues.removeAll(myActiveConnections);
        allQueues.stream().findFirst().ifPresent(e -> messageListenerService.startMessageListener(e));
    }

    private void balanceInstances(int externalActiveConnections, Set<Queue> myActiveConnections) {
        if (myActiveConnections.size() > 0 && myActiveConnections.size() - externalActiveConnections >= 2) {
            messageListenerService.stopMessageListener(myActiveConnections.stream().findAny().get());
        }
    }

    private boolean isTriggeredByConcurrentInstance(BalancingMessage message) {
        return instanceInfo.getId() != message.getInstanceId();
    }

}
