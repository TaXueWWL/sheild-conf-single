package com.sheild.conf;

import com.sheild.conf.domain.config.SysConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author wuwl@19pay.com.cn
 * @date 2018-4-17
 * @desc 配置观察者，实际的配置更新执行方
 * key不存在，直接更新value
 */
public class ConfigDirectUpdateObserver implements Observer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigDirectUpdateObserver.class);

    @Override
    public void update(List<Object> context) {
        ConfigHolder.set((String)context.get(0), (SysConfig)context.get(1));
        LOGGER.debug("通知配置观察者ConfigDirectUpdateObserver更新key={}的配置完成", (String)context.get(0));
    }
}
