package cn.com.kissshot.consumer.bilibili.magicmarket.goods;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@RocketMQMessageListener(consumerGroup = "kissshot-job", topic = "Bilibili_MagicMarket_Goods_Monitor")
public class BilibiliMagicMarketGoodsMonitorListener implements RocketMQListener<String> {

    private final Logger logger = LoggerFactory.getLogger(BilibiliMagicMarketGoodsMonitorListener.class);


    @Override
    public void onMessage(String message) {
        if (null != message) {
            System.out.println(message);
        } else {
            logger.error("消息异常！接收BilibiliMagicMarketMonitor实体为null！");
        }
    }
}
