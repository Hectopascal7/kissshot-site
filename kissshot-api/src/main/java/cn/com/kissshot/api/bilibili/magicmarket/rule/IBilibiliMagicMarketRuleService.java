package cn.com.kissshot.api.bilibili.magicmarket.rule;

import cn.com.kissshot.entity.bilibili.magicmarket.goods.BilibiliMagicMarketGoods;
import cn.com.kissshot.entity.bilibili.magicmarket.rule.BilibiliMagicMarketRule;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IBilibiliMagicMarketRuleService extends IService<BilibiliMagicMarketRule> {

    List<BilibiliMagicMarketRule> getEnabledRuleList();

}
