package cn.com.kissshot.service.system.systemconfig;

import cn.com.kissshot.api.system.systemconfig.ISystemConfigService;
import cn.com.kissshot.entity.system.systemconfig.SystemConfig;
import cn.com.kissshot.mapper.system.systemconfig.SystemConfigMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class SystemConfigService extends ServiceImpl<SystemConfigMapper, SystemConfig> implements ISystemConfigService {

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Override
    public SystemConfig getSystemConfigByConfigName(String configName) {
        return systemConfigMapper.getSystemConfigByConfigName(configName);
    }
}
