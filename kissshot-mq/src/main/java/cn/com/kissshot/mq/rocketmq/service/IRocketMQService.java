package cn.com.kissshot.mq.rocketmq.service;

public interface IRocketMQService {

    public void sendRocketMQMessage(String topic, String message);

}
