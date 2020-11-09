package ahk.solace.receiver;

import org.springframework.boot.SpringApplication;

public class SpringBootReceiver4 extends SpringBootReceiver {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SpringBootReceiver4.class);
        application.run(args);
    }

}