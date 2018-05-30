package com.sheild.conf;

import com.sheild.conf.domain.config.SysConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wuwl@19pay.com.cn
 * @date 2018-4-17
 * @desc 配置项持有者
 */
@Service
class ConfigHolder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigHolder.class);

    private static Map<String, SysConfig> config;

    static {
        config = new ConcurrentHashMap<>();
    }

    @Autowired
    ConfigRepository configRepository;

    @PostConstruct
    private void init() {
        // 初始化加载一次全量配置
        config = configRepository.getConfigMap();
        LOGGER.info("sheild-conf初始化全量配置完成");
    }

    protected static Map<String, SysConfig> getConfig() {
        return config;
    }

    protected static void setConfig(Map<String, SysConfig> config) {
        ConfigHolder.config = config;
    }

    protected static SysConfig get(String key) {
        return config.get(key);
    }

    public static void set(String key, SysConfig sysConfig) {
        config.put(key, sysConfig);
    }

    protected static void remove(String key) {
        config.remove(key);
    }


}
