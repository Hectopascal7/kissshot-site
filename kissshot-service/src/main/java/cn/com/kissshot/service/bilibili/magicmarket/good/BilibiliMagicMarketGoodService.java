package cn.com.kissshot.service.bilibili.magicmarket.good;

import cn.com.kissshot.api.bilibili.magicmarket.good.IBilibiliMagicMarketGoodService;
import cn.com.kissshot.entity.bilibili.magicmarket.good.BilibiliMagicMarketGood;
import cn.com.kissshot.mapper.bilibili.magicmarket.good.BilibiliMagicMarketGoodMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class BilibiliMagicMarketGoodService extends ServiceImpl<BilibiliMagicMarketGoodMapper, BilibiliMagicMarketGood> implements IBilibiliMagicMarketGoodService {

    @Autowired
    private BilibiliMagicMarketGoodMapper marketGoodMapper;

    @Override
    public Integer getMaxBatch(String date) {
        return marketGoodMapper.getMaxBatch(date);
    }

}
