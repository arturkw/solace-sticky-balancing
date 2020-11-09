package ahk.solace.receiver;

import org.springframework.boot.SpringApplication;

public class SpringBootReceiver3 extends SpringBootReceiver {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SpringBootReceiver3.class);
        application.run(args);
    }

}