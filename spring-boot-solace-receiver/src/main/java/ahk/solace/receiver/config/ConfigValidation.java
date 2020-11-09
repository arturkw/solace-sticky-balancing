package ahk.solace.receiver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class ConfigValidation {

    @Value("${message.listener.duration.sec}")
    private int messageListenerDuration;

    @Value("${cron.balancing.expression}")
    private String cronExpression;

    @PostConstruct
    void validateConfiguration() {

        String jobExecutionPeriod = cronExpression.replaceAll("[*]", "").replaceAll(" ", "").replaceAll("/", "");
        int cronSeconds = Integer.parseInt(jobExecutionPeriod);
        if (cronSeconds < messageListenerDuration) {
            throw new Error("Increase duration period of cron job");
        }

    }
}
