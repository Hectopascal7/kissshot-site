package cn.com.kissshot.entity.bilibili.magicmarket.monitor;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("bilibili_magicmarket_rule")
public class BilibiliMagicMarketMonitor {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long c2cItemsId;
    private String c2cItemsName;
    private Long skuId;
    private String name;
    private Long marketPrice;
    private Long price;
    private Double showPrice;
    private Long goodsId;
    private Long ruleId;
    private Date insertTime;

}
