package ahk.solace.receiver.config;

import ahk.solace.receiver.jms.BalancingMessageConverter;
import ahk.solace.receiver.jms.SolaceMessageListener;
import ahk.solace.receiver.model.InstanceInfo;
import ahk.solace.receiver.model.Queue;
import com.solacesystems.jms.SolConnectionFactory;
import com.solacesystems.jms.SolJmsUtility;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.jms.ConnectionFactory;
import javax.jms.Topic;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class SolaceJmsConfiguration {

    @Bean
    InstanceInfo instanceInfo() {
        return new InstanceInfo(Instant.now().hashCode());
    }

    @Bean
    SolConnectionFactory topicConnectionFactory(SolaceProperties solaceProperties) throws Exception {
        SolConnectionFactory cf = SolJmsUtility.createConnectionFactory();
        cf.setHost(solaceProperties.getHost());
        cf.setUsername(solaceProperties.getClientUserName());
        cf.setPassword(solaceProperties.getClientPassword());
        cf.setVPN(solaceProperties.getMsgVpn());
        cf.setDirectTransport(false);
        return cf;
    }

    @Bean
    BalancingMessageConverter balancingMessageConverter() {
        return new BalancingMessageConverter();
    }

    @Bean
    JmsTemplate topicJmsTemplate(SolConnectionFactory topicConnectionFactory, BalancingMessageConverter balancingMessageConverter) {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(topicConnectionFactory);
        jmsTemplate.setMessageConverter(balancingMessageConverter);
        jmsTemplate.setPubSubNoLocal(true);
        return jmsTemplate;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsContainerFactory(BalancingMessageConverter balancingMessageConverter, SolConnectionFactory topicConnectionFactory) {
        DefaultJmsListenerContainerFactory containerFactory = new DefaultJmsListenerContainerFactory();
        containerFactory.setPubSubDomain(true);
        containerFactory.setConnectionFactory(topicConnectionFactory);
        containerFactory.setMessageConverter(balancingMessageConverter);
        return containerFactory;
    }

    @Bean
    DefaultMessageListenerContainer messageListenerForQUEUE1(ConnectionFactory connectionFactory) {
        return createListener(connectionFactory, Queue.QUEUE1);
    }

    @Bean
    DefaultMessageListenerContainer messageListenerForQUEUE2(ConnectionFactory connectionFactory) {
        return createListener(connectionFactory, Queue.QUEUE2);
    }

    @Bean
    DefaultMessageListenerContainer messageListenerForQUEUE3(ConnectionFactory connectionFactory) {
        return createListener(connectionFactory, Queue.QUEUE3);
    }

    @Bean
    DefaultMessageListenerContainer messageListenerForQUEUE4(ConnectionFactory connectionFactory) {
        return createListener(connectionFactory, Queue.QUEUE4);
    }

    @Bean
    Map<Queue, DefaultMessageListenerContainer> listenerContainerMap(DefaultMessageListenerContainer messageListenerForQUEUE1,
                                                                     DefaultMessageListenerContainer messageListenerForQUEUE2,
                                                                     DefaultMessageListenerContainer messageListenerForQUEUE3,
                                                                     DefaultMessageListenerContainer messageListenerForQUEUE4) {
        Map<Queue, DefaultMessageListenerContainer> map = new HashMap<>();
        map.put(Queue.QUEUE1, messageListenerForQUEUE1);
        map.put(Queue.QUEUE2, messageListenerForQUEUE2);
        map.put(Queue.QUEUE3, messageListenerForQUEUE3);
        map.put(Queue.QUEUE4, messageListenerForQUEUE4);
        return map;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(DefaultJmsListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory listenerFactory =
                new DefaultJmsListenerContainerFactory();
        configurer.configure(listenerFactory, connectionFactory);
        listenerFactory.setTransactionManager(null);
        listenerFactory.setSessionTransacted(false);
        return listenerFactory;
    }

    @Bean
    Topic balancingTopic(JmsTemplate topicJmsTemplate) throws Exception {
        return topicJmsTemplate.getConnectionFactory().createConnection()
                .createSession(false, 1).createTopic("BALANCING_TOPIC");
    }

    private DefaultMessageListenerContainer createListener(ConnectionFactory connectionFactory, Queue queue) {
        DefaultMessageListenerContainer messageListenerContainer = new DefaultMessageListenerContainer();
        messageListenerContainer.setConnectionFactory(connectionFactory);
        messageListenerContainer.setDestinationName(queue.getQueueName());
        messageListenerContainer.setMessageListener(new SolaceMessageListener());
        return messageListenerContainer;
    }

}
