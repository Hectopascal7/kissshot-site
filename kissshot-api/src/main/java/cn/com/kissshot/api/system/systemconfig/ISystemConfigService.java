package cn.com.kissshot.api.system.systemconfig;

import cn.com.kissshot.entity.system.systemconfig.SystemConfig;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ISystemConfigService extends IService<SystemConfig> {

    SystemConfig getSystemConfigByConfigName(String configName);

    String getSystemConfigValueByConfigName(String configName);
}
