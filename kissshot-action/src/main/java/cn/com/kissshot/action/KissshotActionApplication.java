package cn.com.kissshot.action;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("cn.com.kissshot.mapper")
@SpringBootApplication(scanBasePackages = "cn.com.kissshot")
public class KissshotActionApplication {

    public static void main(String[] args) {
        SpringApplication.run(KissshotActionApplication.class, args);
    }

}
