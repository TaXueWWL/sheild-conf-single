package com.sheild.conf;

/**
 * @author wuwl@19pay.com.cn
 * @date 2018-4-17
 * @desc 对外暴露的配置获取类
 */
public class Config {

    private volatile static Config config;

    private Config() {}

    public static Config getInstance() {
        if (config == null) {
            synchronized (Config.class) {
                if (config == null) {
                    config = new Config();
                }
            }
        }
        return config;
    }

    /**
     * 客户端获取配置项
     * @param key
     * @return
     */
    public static String get(String key) {
        return getInstance().get(key, "");
    }

    /**
     * 客户端获取配置项，如果不存在则使用默认值
     * @param key
     * @return
     */
    public static String get(String key, String defaultValue) {
        return ConfigHolder.get(key).getConfigValue() == null ? defaultValue : ConfigHolder.get(key).getConfigValue();
    }


}
