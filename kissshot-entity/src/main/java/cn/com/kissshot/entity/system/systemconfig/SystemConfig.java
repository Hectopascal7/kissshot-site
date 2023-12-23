package cn.com.kissshot.entity.system.systemconfig;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("system_config")
public class SystemConfig {

    @TableId
    private String configGuid;

    private String configName;

    private String configValue;

    private String configCategoryGuid;

    private String remark;

    private String description;

}
