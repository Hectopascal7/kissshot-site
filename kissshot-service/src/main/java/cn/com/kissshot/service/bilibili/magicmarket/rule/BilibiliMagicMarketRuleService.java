package cn.com.kissshot.service.bilibili.magicmarket.rule;

import cn.com.kissshot.api.bilibili.magicmarket.goods.IBilibiliMagicMarketGoodsService;
import cn.com.kissshot.api.bilibili.magicmarket.rule.IBilibiliMagicMarketRuleService;
import cn.com.kissshot.entity.bilibili.magicmarket.goods.BilibiliMagicMarketGoods;
import cn.com.kissshot.entity.bilibili.magicmarket.rule.BilibiliMagicMarketRule;
import cn.com.kissshot.mapper.bilibili.magicmarket.good.BilibiliMagicMarketGoodsMapper;
import cn.com.kissshot.mapper.bilibili.magicmarket.rule.BilibiliMagicMarketRuleMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BilibiliMagicMarketRuleService extends ServiceImpl<BilibiliMagicMarketRuleMapper, BilibiliMagicMarketRule> implements IBilibiliMagicMarketRuleService {

    @Autowired
    private BilibiliMagicMarketRuleMapper bilibiliMagicMarketRuleMapper;

    @Override
    public List<BilibiliMagicMarketRule> getEnabledRuleList() {
        return bilibiliMagicMarketRuleMapper.getEnabledRuleList();
    }
}
