package cn.com.kissshot.api.bilibili.magicmarket.goods;

import cn.com.kissshot.entity.bilibili.magicmarket.goods.BilibiliMagicMarketGoods;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IBilibiliMagicMarketGoodsService extends IService<BilibiliMagicMarketGoods> {
    Integer getMaxBatch(String date);

    List<BilibiliMagicMarketGoods> getMagicMarketGoodsList();
}
