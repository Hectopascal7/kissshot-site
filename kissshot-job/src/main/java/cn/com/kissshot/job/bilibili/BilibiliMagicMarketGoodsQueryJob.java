package cn.com.kissshot.job.bilibili;

import cn.com.kissshot.api.bilibili.magicmarket.goods.IBilibiliMagicMarketGoodsService;
import cn.com.kissshot.api.bilibili.magicmarket.rule.IBilibiliMagicMarketRuleService;
import cn.com.kissshot.api.system.systemconfig.ISystemConfigService;
import cn.com.kissshot.entity.bilibili.magicmarket.goods.BilibiliMagicMarketGoods;
import cn.com.kissshot.entity.bilibili.magicmarket.rule.BilibiliMagicMarketRule;
import cn.com.kissshot.entity.system.systemconfig.SystemConfig;
import cn.com.kissshot.mail.service.IMailService;
import cn.com.kissshot.mq.rocketmq.service.IRocketMQService;
import cn.com.kissshot.util.date.KissshotDateUtil;
import cn.com.kissshot.util.date.constant.KissshotDateConstant;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@EnableScheduling
public class BilibiliMagicMarketGoodsQueryJob {

    @Autowired
    public RestTemplate restTemplate;

    @Autowired
    public IBilibiliMagicMarketGoodsService iBilibiliMagicMarketGoodsService;

    @Autowired
    public IBilibiliMagicMarketRuleService iBilibiliMagicMarketRuleService;

    @Autowired
    public IRocketMQService iRocketMQService;

    @Autowired
    public IMailService iMailService;

    @Autowired
    public ISystemConfigService iSystemConfigService;

    private final Logger logger = LoggerFactory.getLogger(BilibiliMagicMarketGoodsQueryJob.class);

    private List<BilibiliMagicMarketRule> bilibiliMagicMarketRuleList = new ArrayList<>();

    /**
     * 每3小时执行1次，0时、12时全量执行，其余时间只搜索前1000条
     */
    @Scheduled(cron = "00 00 0/4 ? * *")
    public void executeQuery() {
        logger.info("==========哔哩哔哩市集商品获取作业开始！==========");
        bilibiliMagicMarketRuleList = iBilibiliMagicMarketRuleService.getEnabledRuleList();
        String url = iSystemConfigService.getSystemConfigValueByConfigName("Bilibili市集商品列表接口地址");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<String> cookieList = new ArrayList<>();
        SystemConfig systemConfig = iSystemConfigService.getSystemConfigByConfigName("Bilibili市集接口用户SESSION");
        String cookie = "";
        if (null != systemConfig) {
            cookie = systemConfig.getConfigValue();
        }
        cookieList.add(cookie);
        headers.put("Cookie", cookieList);
        // 获取批次号
        Integer batch = iBilibiliMagicMarketGoodsService.getMaxBatch(KissshotDateUtil.convertDateToString(new Date(), KissshotDateConstant.YYYY_MM_DD));
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int sign = 100;
        if (hour % 12 == 0) {
            sign = -1;
        }
        // 首次调用,nextId为空
        queryMagicMarketGoods(url, headers, null, batch, sign);
        logger.info("==========哔哩哔哩市集商品获取作业结束！==========");
    }

    public void queryMagicMarketGoods(String url, HttpHeaders headers, String nextId, Integer batch, Integer sign) {
        if (sign == 0) {
            return;
        }
        JSONObject body = new JSONObject();
        if (StringUtils.isNotBlank(nextId)) {
            body.put("nextId", nextId);
        }
        if (batch % 2 == 0) {
            body.put("categoryFilter", iSystemConfigService.getSystemConfigByConfigName("Bilibili市集商品列表接口手办类目").getConfigValue());
        } else {
            body.put("categoryFilter", iSystemConfigService.getSystemConfigByConfigName("Bilibili市集商品列表接口周边类目").getConfigValue());
        }
        JSONArray priceFilters = new JSONArray();
        priceFilters.add("20000-0");
        body.put("priceFilters", priceFilters);
        HttpEntity<String> request = new HttpEntity<>(body.toJSONString(), headers);

        // 每次调用接口前先发送消息通知
        sendMonitor();

        JSONObject rtnObj = restTemplate.postForEntity(url, request, JSONObject.class).getBody();

        sign--;

        if (null != rtnObj) {
            Integer code = rtnObj.getInteger("code");
            if (null != code && code == 0) {
                JSONObject data = rtnObj.getJSONObject("data");
                if (null != data) {
                    nextId = data.getString("nextId");
                    JSONArray dataArr = data.getJSONArray("data");
                    if (null != dataArr && !dataArr.isEmpty()) {
                        analyzeFigures(dataArr, batch);
                    } else {
                        logger.error("错误！空数据，dataArr为空！");
                        logger.info("[返回值]:" + rtnObj.toJSONString());
                    }
                    // 非首次调用，nextId不为空才可以继续调用
                    if (StringUtils.isNotBlank(nextId)) {
                        int mSeconds = 8000 + (int) (Math.random() * (10000 - 8000 + 1));
                        try {
                            Thread.sleep(mSeconds);
                        } catch (InterruptedException e) {
                            logger.error("错误，线程等待出现异常！");
                        }

                        // 非首次调用，nextId不为空才可以继续调用
                        queryMagicMarketGoods(url, headers, nextId, batch, sign);
                    } else {
                        logger.info("已至末尾，任务结束！");
                    }
                }
            } else {
                logger.error("错误！访问异常，Code值不为0！");
                logger.error("[异常返回值]：" + rtnObj.toJSONString());
            }
        } else {
            logger.error("错误！调用接口未返回响应，RtnObj为空值！");
        }
    }

    /**
     * 分析商品数据并保存到数据表
     */
    public void analyzeFigures(JSONArray dataArr, Integer batch) {
        for (int i = 0; i < dataArr.size(); i++) {
            JSONObject figure = dataArr.getJSONObject(i);
            if (null != figure) {
                BilibiliMagicMarketGoods magicMarketGoods = new BilibiliMagicMarketGoods();
                Long c2cItemsId = figure.getLong("c2cItemsId");
                magicMarketGoods.setC2cItemsId(c2cItemsId);
                Integer type = figure.getInteger("type");
                magicMarketGoods.setType(type);
                String c2cItemsName = StringUtils.trim(figure.getString("c2cItemsName"));
                magicMarketGoods.setC2cItemsName(c2cItemsName);
                JSONArray detailDtoList = figure.getJSONArray("detailDtoList");
                JSONObject detail = detailDtoList.getJSONObject(0);
                Long blindBoxId = detail.getLong("blindBoxId");
                magicMarketGoods.setBlindBoxId(blindBoxId);
                Long itemsId = detail.getLong("itemsId");
                magicMarketGoods.setItemsId(itemsId);
                Long skuId = detail.getLong("skuId");
                magicMarketGoods.setSkuId(skuId);
                String name = StringUtils.trim(detail.getString("name"));
                magicMarketGoods.setName(name);
                String img = detail.getString("img");
                magicMarketGoods.setImg(img);
                Long marketPrice = detail.getLong("marketPrice");
                magicMarketGoods.setMarketPrice(marketPrice);
                Integer goodType = detail.getInteger("type");
                magicMarketGoods.setGoodType(goodType);
                Boolean isHidden = detail.getBoolean("isHidden");
                if (isHidden) {
                    magicMarketGoods.setIsHidden(1);
                } else {
                    magicMarketGoods.setIsHidden(0);
                }
                Integer totalItemsCount = figure.getInteger("totalItemsCount");
                magicMarketGoods.setTotalItemsCount(totalItemsCount);
                Long price = figure.getLong("price");
                magicMarketGoods.setPrice(price);
                Double showPrice = Double.valueOf(figure.getString("showPrice"));
                magicMarketGoods.setShowPrice(showPrice);
                String uid = figure.getString("uid");
                magicMarketGoods.setUid(uid);
                Integer paymentTime = figure.getInteger("paymentTime");
                magicMarketGoods.setPaymentTime(paymentTime);
                Boolean isMyPublish = figure.getBoolean("isMyPublish");
                if (isMyPublish) {
                    magicMarketGoods.setIsMyPublish(1);
                } else {
                    magicMarketGoods.setIsMyPublish(0);
                }
                String uname = figure.getString("uname");
                magicMarketGoods.setUname(uname);
                String uface = figure.getString("uface");
                magicMarketGoods.setUface(uface);
                magicMarketGoods.setInsertTime(new Date());
                magicMarketGoods.setBatch(batch + 1);
                iBilibiliMagicMarketGoodsService.save(magicMarketGoods);

                for (BilibiliMagicMarketRule bilibiliMagicMarketRule : bilibiliMagicMarketRuleList) {
                    if (bilibiliMagicMarketRule.getSkuId().equals(magicMarketGoods.getSkuId()) && bilibiliMagicMarketRule.getThresholdValue() >= magicMarketGoods.getPrice()) {
                        String topic = iSystemConfigService.getSystemConfigValueByConfigName("Bilibili市集商品监控MQ消息主题");
                        String message = "监控成功！商品：[" + magicMarketGoods.getName() + "]，" + "价格：[" + (magicMarketGoods.getShowPrice() / 100) + "元]" + "，阈值：[" + (bilibiliMagicMarketRule.getThresholdValue() / 100) + "元]，较阈值便宜：[" + ((bilibiliMagicMarketRule.getThresholdValue() - magicMarketGoods.getPrice()) / 100) + "元]。";
                        // 暂时注释MQ消息发送
                        // iRocketMQService.sendRocketMQMessage(topic, message);
                        String text = "商品：" + name + "；价格：" + showPrice + "元；阈值：" + (bilibiliMagicMarketRule.getThresholdValue() / 100) + "元；c2cItemsId：" + c2cItemsId;
                        // 邮件通知
                        iMailService.sendSimpleMail("[市集监控]" + name + "监控成功！", text, "704825080@qq.com");
                        logger.info(message);
                    }
                }

                logger.info(c2cItemsName);
            }
        }
    }


    /**
     * 发送监控数据
     */
    public void sendMonitor() {
        String url = iSystemConfigService.getSystemConfigValueByConfigName("Bilibili市集商品通知接口地址");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String monitorJSONString = iSystemConfigService.getSystemConfigValueByConfigName("Bilibili市集商品通知接口Body数据");
        JSONObject body = JSON.parseObject(monitorJSONString);
        HttpEntity<String> request = new HttpEntity<>(body.toJSONString(), headers);
        restTemplate.postForEntity(url, request, JSONObject.class).getBody();
    }

}
