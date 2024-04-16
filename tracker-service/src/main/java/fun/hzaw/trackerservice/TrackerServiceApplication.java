package fun.hzaw.trackerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TrackerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrackerServiceApplication.class, args);
    }

}
