package cn.com.kissshot.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "cn.com.kissshot")
public class KissshotConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(KissshotConsumerApplication.class, args);
	}

}
