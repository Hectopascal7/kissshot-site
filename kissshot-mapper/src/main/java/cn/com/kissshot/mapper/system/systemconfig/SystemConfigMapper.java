package cn.com.kissshot.mapper.system.systemconfig;

import cn.com.kissshot.entity.system.systemconfig.SystemConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface SystemConfigMapper extends BaseMapper<SystemConfig> {


    @Select("select * from system_config where configName = #{configName}")
    SystemConfig getSystemConfigByConfigName(@Param("configName") String configName);

    @Select("select configValue from system_config where configName = #{configName}")
    String getSystemConfigValueByConfigName(String configName);
}
