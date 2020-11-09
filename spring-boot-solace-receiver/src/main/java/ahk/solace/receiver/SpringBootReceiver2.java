package ahk.solace.receiver;

import org.springframework.boot.SpringApplication;

public class SpringBootReceiver2 extends SpringBootReceiver {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SpringBootReceiver2.class);
        application.run(args);
    }

}