package cn.com.kissshot.mq.rocketmq.service;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RocketMQService implements IRocketMQService {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public void sendRocketMQMessage(String topic, String message) {
        rocketMQTemplate.convertAndSend(topic, message);
    }

}
