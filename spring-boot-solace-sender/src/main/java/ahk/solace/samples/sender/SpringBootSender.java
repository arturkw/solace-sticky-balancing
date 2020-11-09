package ahk.solace.samples.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableScheduling
public class SpringBootSender {

    private static long queue1Counter = 0;
    private static long queue2Counter = 0;
    private static long queue3Counter = 0;
    private static long queue4Counter = 0;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSender.class, args);
    }

    @Autowired
    private JmsTemplate jmsTemplate;

    @PostConstruct
    private void customizeJmsTemplate() {
        CachingConnectionFactory ccf = new CachingConnectionFactory();
        ccf.setTargetConnectionFactory(jmsTemplate.getConnectionFactory());
        jmsTemplate.setConnectionFactory(ccf);
        jmsTemplate.setPubSubDomain(false);
    }

    @Value("SpringTestQueue")
    private String queueName;

    @Scheduled(fixedRate = 3000)
    public void sendEvent1() throws Exception {
        String msg = "Hello World from Queue 1. 		Message count: " + queue1Counter++;
        System.out.println("==========SENDING MESSAGE========== " + msg);
        jmsTemplate.convertAndSend("QUEUE1", msg);

    }

    @Scheduled(fixedRate = 3000)
    public void sendEvent2() throws Exception {
        String msg = "Hello World from Queue 2. 		Message count: " + queue2Counter++;
        System.out.println("==========SENDING MESSAGE========== " + msg);
        jmsTemplate.convertAndSend("QUEUE2", msg);
    }

    @Scheduled(fixedRate = 3000)
    public void sendEvent3() throws Exception {
        String msg = "Hello World from Queue 3. 		Message count: " + queue3Counter++;
        System.out.println("==========SENDING MESSAGE========== " + msg);
        jmsTemplate.convertAndSend("QUEUE3", msg);
    }

    @Scheduled(fixedRate = 3000)
    public void sendEvent4() throws Exception {
        String msg = "Hello World from Queue 4. 		Message count: " + queue4Counter++;
        System.out.println("==========SENDING MESSAGE========== " + msg);
        jmsTemplate.convertAndSend("QUEUE4", msg);
    }

}
