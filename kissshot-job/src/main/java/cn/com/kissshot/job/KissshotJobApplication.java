package cn.com.kissshot.job;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("cn.com.kissshot.mapper")
@SpringBootApplication(scanBasePackages = "cn.com.kissshot")
public class KissshotJobApplication {

    public static void main(String[] args) {
        SpringApplication.run(KissshotJobApplication.class, args);
    }

}
