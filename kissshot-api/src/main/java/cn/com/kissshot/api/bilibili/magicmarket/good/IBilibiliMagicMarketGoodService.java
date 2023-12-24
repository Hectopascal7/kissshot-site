package cn.com.kissshot.api.bilibili.magicmarket.good;

import cn.com.kissshot.entity.bilibili.magicmarket.good.BilibiliMagicMarketGood;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IBilibiliMagicMarketGoodService extends IService<BilibiliMagicMarketGood> {
    Integer getMaxBatch(String date);

    List<BilibiliMagicMarketGood> getMagicMarketGoodsList();
}
