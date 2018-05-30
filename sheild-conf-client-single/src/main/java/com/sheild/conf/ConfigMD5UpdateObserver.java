package com.sheild.conf;

import com.sheild.conf.domain.config.SysConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author wuwl@19pay.com.cn
 * @date 2018-4-17
 * @desc 基于MD5进行更新观察者
 */
public class ConfigMD5UpdateObserver implements Observer  {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigDirectUpdateObserver.class);

    @Override
    public void update(List<Object> context) {
        ConfigHolder.getConfig().remove(context.get(0));
        ConfigHolder.getConfig().put((String)context.get(0), (SysConfig)context.get(1));
        LOGGER.debug("通知配置观察者ConfigMD5UpdateObserver根据MD5更新key={}的配置完成", context.get(0));
    }
}
