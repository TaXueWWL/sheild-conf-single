package com.sheild.conf.config;


import com.sheild.conf.domain.config.SysConfig;

import java.util.List;

/**
 * @author wuwl@19pay.com.cn
 * @date 2018-4-2
 * @desc 配置服务接口定义
 */
public interface IConfigService {

    public void set(SysConfig sysConfig);

    public SysConfig get(String key, String projectName);

    public List<SysConfig> fetchAll(String projectName);
}
