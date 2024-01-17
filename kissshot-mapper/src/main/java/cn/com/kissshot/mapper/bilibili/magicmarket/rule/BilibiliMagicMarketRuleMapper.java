package cn.com.kissshot.mapper.bilibili.magicmarket.rule;

import cn.com.kissshot.entity.bilibili.magicmarket.rule.BilibiliMagicMarketRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BilibiliMagicMarketRuleMapper extends BaseMapper<BilibiliMagicMarketRule> {


    @Select("select * from bilibili_magicmarket_rule where isEnabled=1")
    List<BilibiliMagicMarketRule> getEnabledRuleList();

}
