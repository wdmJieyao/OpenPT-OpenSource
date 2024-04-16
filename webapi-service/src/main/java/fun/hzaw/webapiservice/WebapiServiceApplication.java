package fun.hzaw.webapiservice;

import cn.dev33.satoken.SaManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class WebapiServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebapiServiceApplication.class, args);
    }

}
