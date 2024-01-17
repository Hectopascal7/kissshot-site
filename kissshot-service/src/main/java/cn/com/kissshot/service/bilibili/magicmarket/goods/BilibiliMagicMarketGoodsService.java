package cn.com.kissshot.service.bilibili.magicmarket.goods;

import cn.com.kissshot.api.bilibili.magicmarket.goods.IBilibiliMagicMarketGoodsService;
import cn.com.kissshot.entity.bilibili.magicmarket.goods.BilibiliMagicMarketGoods;
import cn.com.kissshot.mapper.bilibili.magicmarket.good.BilibiliMagicMarketGoodsMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BilibiliMagicMarketGoodsService extends ServiceImpl<BilibiliMagicMarketGoodsMapper, BilibiliMagicMarketGoods> implements IBilibiliMagicMarketGoodsService {

    @Autowired
    private BilibiliMagicMarketGoodsMapper bilibiliMagicMarketGoodsMapper;

    @Override
    public Integer getMaxBatch(String date) {
        return bilibiliMagicMarketGoodsMapper.getMaxBatch(date);
    }

    @Override
    public List<BilibiliMagicMarketGoods> getMagicMarketGoodsList() {
        return bilibiliMagicMarketGoodsMapper.getMagicMarketGoodsList();
    }

}
