package cn.com.kissshot.entity.mq;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("bilibili_magicmarket_rule")
public class SystemMQ {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long skuId;
    private String c2cItemsName;
    private Long marketPrice;
    private Long thresholdValue;
    private Integer isEnabled;

}
