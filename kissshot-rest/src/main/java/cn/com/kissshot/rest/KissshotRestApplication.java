package cn.com.kissshot.rest;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@MapperScan("cn.com.kissshot.mapper.*")
@SpringBootApplication(scanBasePackages = "cn.com.kissshot.*")
public class KissshotRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(KissshotRestApplication.class, args);
    }

}
