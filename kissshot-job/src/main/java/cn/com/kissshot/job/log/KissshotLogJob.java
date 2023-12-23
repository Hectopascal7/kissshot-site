package cn.com.kissshot.job.log;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
public class KissshotLogJob {

    public void execLogJob() {
        System.out.println("exec");
    }

}
