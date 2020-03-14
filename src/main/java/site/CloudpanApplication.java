package site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableConfigurationProperties
@SpringBootApplication
public class CloudpanApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudpanApplication.class, args);
    }

}
