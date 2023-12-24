package cn.com.kissshot.mapper.bilibili.magicmarket.good;

import cn.com.kissshot.entity.bilibili.magicmarket.good.BilibiliMagicMarketGood;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BilibiliMagicMarketGoodMapper extends BaseMapper<BilibiliMagicMarketGood> {


    @Select("select IFNULL(MAX(batch),0) from bilibili_magicmarket_goods where DATE_FORMAT(inserttime,'%Y-%m-%d') = #{date}")
    Integer getMaxBatch(@Param("date") String date);

    List<BilibiliMagicMarketGood> getGoodListBySkuId(@Param("skuId") String skuId);

    @Select("select * from bilibili_magicmarket_goods order by insertTime desc limit 10")
    List<BilibiliMagicMarketGood> getMagicMarketGoodsList();
}
