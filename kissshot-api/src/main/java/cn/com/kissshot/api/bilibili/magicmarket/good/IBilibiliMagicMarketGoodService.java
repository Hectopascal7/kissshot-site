package cn.com.kissshot.api.bilibili.magicmarket.good;

import cn.com.kissshot.entity.bilibili.magicmarket.good.BilibiliMagicMarketGood;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IBilibiliMagicMarketGoodService extends IService<BilibiliMagicMarketGood> {
    Integer getMaxBatch(String date);

}
