package cn.com.kissshot.action.bilibili.mall;

import cn.com.kissshot.api.bilibili.magicmarket.good.IBilibiliMagicMarketGoodService;
import cn.com.kissshot.api.system.systemconfig.ISystemConfigService;
import cn.com.kissshot.entity.bilibili.magicmarket.good.BilibiliMagicMarketGood;
import cn.com.kissshot.entity.system.systemconfig.SystemConfig;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/bilibiliMall")
public class BilibiliMallController {

    @Autowired
    public RestTemplate restTemplate;

    @Autowired
    public IBilibiliMagicMarketGoodService iBilibiliMagicMarketGoodService;

    @Autowired
    public ISystemConfigService iSystemConfigService;

    public Integer batch;

    private final Logger logger = LoggerFactory.getLogger(BilibiliMallController.class);

    @GetMapping("/getMagicMarketGoods")
    public JSONObject getMagicMarketGoods() throws InterruptedException {
        Integer code;
        batch = iBilibiliMagicMarketGoodService.getMaxBatch(KissshotDateUtil.convertDateToString(new Date(), KissshotDateConstant.YYYY_MM_DD));
        JSONObject rtnObj = queryMagicMaketGoods(null);
        if (null != rtnObj) {
            code = rtnObj.getInteger("code");
            if (code == 0) {
                JSONObject data = rtnObj.getJSONObject("data");
                if (null != data) {
                    String nextId = data.getString("nextId");
                    JSONArray dataArr = data.getJSONArray("data");
                    if (null != dataArr && !dataArr.isEmpty()) {
                        analyzeFigure(dataArr);
                        // 调用下一页
                        boolean flag = true;
                        while (flag && StringUtils.isNotBlank(nextId)) {
                            Thread.sleep(5000);
                            JSONObject nextPageObj = queryMagicMaketGoods(nextId);
                            if (nextPageObj.getInteger("code") == 0) {
                                JSONObject data2nd = nextPageObj.getJSONObject("data");
                                if (null != data2nd) {
                                    JSONArray dataArr2nd = data2nd.getJSONArray("data");
                                    analyzeFigure(dataArr2nd);
                                    nextId = data2nd.getString("nextId");
                                } else {
                                    flag = false;
                                }
                            } else {
                                flag = false;
                                logger.error("访问异常！");
                                logger.error(nextPageObj.toJSONString());
                            }
                        }
                    } else {
                        logger.error("空数据！");
                    }
                }
            } else {
                logger.error("访问异常！");
                logger.error(rtnObj.toJSONString());
            }
            JSONObject result = new JSONObject();
            result.put("code", 1);
            result.put("msg", "查询完成！");
            return result;
        } else {
            JSONObject result = new JSONObject();
            result.put("code", 0);
            result.put("msg", "接口返回值为空！");
            return result;
        }
    }

    public JSONObject queryMagicMaketGoods(String nextId) {
        String url = "https://mall.bilibili.com/mall-magic-c/internet/c2c/v2/list";
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
        JSONObject body = new JSONObject();
        body.put("nextId", nextId);
        if ((batch + 1) % 2 == 0) {
            body.put("categoryFilter", iSystemConfigService.getSystemConfigByConfigName("Bilibili市集商品列表接口周边类目").getConfigValue());
        } else {
            body.put("categoryFilter", iSystemConfigService.getSystemConfigByConfigName("Bilibili市集商品列表接口手办类目").getConfigValue());
        }
        JSONArray priceFilters = new JSONArray();
        priceFilters.add("20000-0");
        body.put("priceFilters", priceFilters);
        HttpEntity<String> request = new HttpEntity<>(body.toJSONString(), headers);
        sendMonitor();
        JSONObject rtnObj = restTemplate.postForEntity(url, request, JSONObject.class).getBody();
        logger.info("[返回值]:" + rtnObj.toJSONString());
        return rtnObj;
    }

    public void analyzeFigure(JSONArray dataArr) {
        for (int i = 0; i < dataArr.size(); i++) {
            JSONObject figure = dataArr.getJSONObject(i);
            if (null != figure) {
                BilibiliMagicMarketGood magicMarketGood = new BilibiliMagicMarketGood();
                Long c2cItemsId = figure.getLong("c2cItemsId");
                magicMarketGood.setC2cItemsId(c2cItemsId);
                Integer type = figure.getInteger("type");
                magicMarketGood.setType(type);
                String c2cItemsName = figure.getString("c2cItemsName");
                magicMarketGood.setC2cItemsName(c2cItemsName);
                JSONArray detailDtoList = figure.getJSONArray("detailDtoList");
                JSONObject detail = detailDtoList.getJSONObject(0);
                Long blindBoxId = detail.getLong("blindBoxId");
                magicMarketGood.setBlindBoxId(blindBoxId);
                Long itemsId = detail.getLong("itemsId");
                magicMarketGood.setItemsId(itemsId);
                Long skuId = detail.getLong("skuId");
                magicMarketGood.setSkuId(skuId);
                String name = detail.getString("name");
                magicMarketGood.setName(name);
                String img = detail.getString("img");
                magicMarketGood.setImg(img);
                Long marketPrice = detail.getLong("marketPrice");
                magicMarketGood.setMarketPrice(marketPrice);
                Integer goodType = detail.getInteger("type");
                magicMarketGood.setGoodType(goodType);
                Boolean isHidden = detail.getBoolean("isHidden");
                if (isHidden) {
                    magicMarketGood.setIsHidden(1);
                } else {
                    magicMarketGood.setIsHidden(0);
                }
                Integer totalItemsCount = figure.getInteger("totalItemsCount");
                magicMarketGood.setTotalItemsCount(totalItemsCount);
                Long price = figure.getLong("price");
                magicMarketGood.setPrice(price);
                Double showPrice = Double.valueOf(figure.getString("showPrice"));
                magicMarketGood.setShowPrice(showPrice);
                String uid = figure.getString("uid");
                magicMarketGood.setUid(uid);
                Integer paymentTime = figure.getInteger("paymentTime");
                magicMarketGood.setPaymentTime(paymentTime);
                Boolean isMyPublish = figure.getBoolean("isMyPublish");
                if (isMyPublish) {
                    magicMarketGood.setIsMyPublish(1);
                } else {
                    magicMarketGood.setIsMyPublish(0);
                }
                String uname = figure.getString("uname");
                magicMarketGood.setUname(uname);
                String uface = figure.getString("uface");
                magicMarketGood.setUface(uface);
                magicMarketGood.setInsertTime(new Date());

                magicMarketGood.setBatch(batch + 1);
                iBilibiliMagicMarketGoodService.save(magicMarketGood);
                logger.info(c2cItemsName);
            }
        }
    }

    public void sendMonitor() {
        String url = "https://mall.bilibili.com/mall-dayu/h5/monitor";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject body = new JSONObject();
        body = JSON.parseObject("{\"event\":\"lens.magic.network\",\"time\":1702483662967,\"mid\":\"15050127\",\"platform\":3,\"buvid\":\"131C268C-C2D3-1AE4-9D38-01931A7EC71D27647infoc\",\"appV\":\"0\",\"ua\":\"Mozilla%2F5.0%20(Linux%3B%20Android%206.0%3B%20Nexus%205%20Build%2FMRA58N)%20AppleWebKit%2F537.36%20(KHTML%2C%20like%20Gecko)%20Chrome%2F120.0.0.0%20Mobile%20Safari%2F537.36\",\"isbili\":0,\"url\":\"mall.bilibili.com%2Fneul-next%2Findex.html%3Fpage%3Dmagic-market_index\",\"referer\":\"-\",\"bid\":\"MagicAwards\",\"lensV\":\"0.2.8\",\"ext\":{\"category\":\"service\",\"from\":\"\",\"msource\":\"\",\"magicNetwork\":{\"method\":\"post\",\"url\":\"//mall.bilibili.com/mall-magic-c/internet/c2c/v2/list\",\"payload\":\"{\\\"categoryFilter\\\":\\\"2312\\\",\\\"priceFilters\\\":[\\\"20000-0\\\"],\\\"nextId\\\":\\\"JwSSQEML0ACMccWnNt3KK7lQB43KRtG436qo7qXdQ2o=\\\"}\",\"duration\":223.40000000596046,\"status\":200,\"code\":0,\"traceid_svr\":\"\",\"response\":\"%7B%22code%22%3A0%2C%22message%22%3A%22success%22%2C%22data%22%3A%7B%22data%22%3A%5B%7B%22c2cItemsId%22%3A20351685749%2C%22type%22%3A1%2C%22c2cItemsName%22%3A%22GOOD%20SMILE%20ARTS%20SHANGHAI%20%E7%82%8E%E7%8B%B1%E8%A3%81%E5%86%B3%E8%80%85%20%E6%89%8B%E5%8A%9E%22%2C%22detailDtoList%22%3A%5B%7B%22blindBoxId%22%3A164936949%2C%22itemsId%22%3A10023982%2C%22skuId%22%3A1000053223%2C%22name%22%3A%22GOOD%20SMILE%20ARTS%20SHANGHAI%20%E7%82%8E%E7%8B%B1%E8%A3%81%E5%86%B3%E8%80%85%20%E6%89%8B%E5%8A%9E%22%2C%22img%22%3A%22%2F%2Fi0.hdslb.com%2Fbfs%2Fmall%2Fmall%2F02%2F83%2F028323f91edb4c935e7fd535442ef2d2.png%22%2C%22marketPrice%22%3A141900%2C%22type%22%3A0%2C%22isHidden%22%3Afalse%7D%5D%2C%22totalItemsCount%22\",\"vitalDataError\":false,\"vitalDataErrorInfo\":\"\"},\"stage\":\"WINDOW_LOADED\"}}");
        HttpEntity<String> request = new HttpEntity<>(body.toJSONString(), headers);
        restTemplate.postForEntity(url, request, JSONObject.class).getBody();
    }

}
