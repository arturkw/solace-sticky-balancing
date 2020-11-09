package ahk.solace.receiver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringBootReceiver {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SpringBootReceiver.class);
        application.run(args);
    }

}