package cn.com.kissshot.entity.bilibili.magicmarket.rule;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("bilibili_magicmarket_rule")
public class BilibiliMagicMarketRule {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long skuId;
    private String c2cItemsName;
    private Long marketPrice;
    private Long thresholdValue;
    private Integer isEnabled;

}
