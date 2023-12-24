package cn.com.kissshot.rest.bilibili.mall;

import cn.com.kissshot.api.bilibili.magicmarket.good.IBilibiliMagicMarketGoodService;
import cn.com.kissshot.entity.bilibili.magicmarket.good.BilibiliMagicMarketGood;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bilibiliMagicMarketGoodsRest")
public class BilibiliMagicMarketGoodsRest {

    @Autowired
    private IBilibiliMagicMarketGoodService iBilibiliMagicMarketGoodService;

    @CrossOrigin
    @PostMapping("/queryBilibiliMagicMarketGoodsData")
    public JSONObject queryBilibiliMagicMarketGoodsData() {
        List<BilibiliMagicMarketGood> bilibiliMagicMarketGoodList = iBilibiliMagicMarketGoodService.getMagicMarketGoodsList();
        JSONArray figureArray = new JSONArray();
        if (CollectionUtils.isNotEmpty(bilibiliMagicMarketGoodList)) {
            for (BilibiliMagicMarketGood bilibiliMagicMarketGood : bilibiliMagicMarketGoodList) {
                JSONObject figure = new JSONObject();
                figure.put("id", bilibiliMagicMarketGood.getId());
                figure.put("owner", bilibiliMagicMarketGood.getUname());
                figure.put("title", bilibiliMagicMarketGood.getC2cItemsName());
                figure.put("cover", "https:" + bilibiliMagicMarketGood.getImg());
                figure.put("avatar", bilibiliMagicMarketGood.getUface());
                figure.put("subDescription", bilibiliMagicMarketGood.getName());
                figure.put("createdAt", bilibiliMagicMarketGood.getInsertTime());
                JSONArray memberArray = new JSONArray();
                JSONObject member = new JSONObject();
                member.put("id", bilibiliMagicMarketGood.getUid());
                member.put("name", bilibiliMagicMarketGood.getUname());
                member.put("avatar", bilibiliMagicMarketGood.getUface());
                memberArray.add(member);
                figure.put("members", memberArray);
                figureArray.add(figure);
            }
        }
        JSONObject listObj = new JSONObject();
        listObj.put("list", figureArray);
        JSONObject rtnObj = new JSONObject();
        rtnObj.put("data", listObj);
        return rtnObj;
    }

}
