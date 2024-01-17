package cn.com.kissshot.entity.bilibili.magicmarket.goods;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("bilibili_magicmarket_goods")
public class BilibiliMagicMarketGoods {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long c2cItemsId;
    private Integer type;
    private String c2cItemsName;
    private Long blindBoxId;
    private Long itemsId;
    private Long skuId;
    private String name;
    private String img;
    private Long marketPrice;
    private Integer goodType;
    private Integer isHidden;
    private Integer totalItemsCount;
    private Long price;
    private Double showPrice;
    private String uid;
    private Integer paymentTime;
    private Integer isMyPublish;
    private String uname;
    private String uface;
    private Date insertTime;
    private Integer batch;

}
