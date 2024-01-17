package cn.com.kissshot.rest.bilibili.mall;

import cn.com.kissshot.api.bilibili.magicmarket.goods.IBilibiliMagicMarketGoodsService;
import cn.com.kissshot.entity.bilibili.magicmarket.goods.BilibiliMagicMarketGoods;
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
    private IBilibiliMagicMarketGoodsService iBilibiliMagicMarketGoodsService;

    @CrossOrigin
    @PostMapping("/queryBilibiliMagicMarketGoodsData")
    public JSONObject queryBilibiliMagicMarketGoodsData() {
        List<BilibiliMagicMarketGoods> bilibiliMagicMarketGoodsList = iBilibiliMagicMarketGoodsService.getMagicMarketGoodsList();
        JSONArray figureArray = new JSONArray();
        if (CollectionUtils.isNotEmpty(bilibiliMagicMarketGoodsList)) {
            for (BilibiliMagicMarketGoods bilibiliMagicMarketGoods : bilibiliMagicMarketGoodsList) {
                JSONObject figure = new JSONObject();
                figure.put("id", bilibiliMagicMarketGoods.getId());
                figure.put("owner", bilibiliMagicMarketGoods.getUname());
                figure.put("title", bilibiliMagicMarketGoods.getC2cItemsName());
                figure.put("cover", "https:" + bilibiliMagicMarketGoods.getImg());
                figure.put("avatar", bilibiliMagicMarketGoods.getUface());
                figure.put("subDescription", bilibiliMagicMarketGoods.getName());
                figure.put("createdAt", bilibiliMagicMarketGoods.getInsertTime());
                JSONArray memberArray = new JSONArray();
                JSONObject member = new JSONObject();
                member.put("id", bilibiliMagicMarketGoods.getUid());
                member.put("name", bilibiliMagicMarketGoods.getUname());
                member.put("avatar", bilibiliMagicMarketGoods.getUface());
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
