package com.sheild.conf;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.sheild.conf.domain.config.SysConfig;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author wuwl@19pay.com.cn
 * @date 2018-4-17
 * @desc 获取配置持久化类
 */
@Repository
class ConfigRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigRepository.class);


    @Autowired
    JdbcTemplate jdbcTemplate;

    protected Map<String, SysConfig> getConfigMap() {
        String sql = SQL.SQL_GET_ALL_CONFIGS;
        LOGGER.debug("开始获取全量配置信息");
        final List<SysConfig> sysConfigs = new CopyOnWriteArrayList<>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                SysConfig sysConfig = new SysConfig();
                sysConfig.setConfigId(rs.getInt("configId"))
                        .setConfigKey(rs.getString("configKey"))
                        .setConfigValue(rs.getString("configValue"))
                        .setConfigDesc(rs.getString("configDesc"))
                        .setOptUser(rs.getString("optUser"))
                        .setInsertTime(rs.getString("insertTime"))
                        .setUpdateTime(rs.getString("updateTime"));
                sysConfigs.add(sysConfig);
            }
        });
        // 迭代设置Map，生成MD5
        Map<String, SysConfig> sysConfigMap = new ConcurrentHashMap<>();
        for (SysConfig sysConfig : sysConfigs) {
            String md5Source = sysConfig.getProjectName() + sysConfig.getConfigKey()
                    + sysConfig.getConfigValue();
            sysConfig.setMd5Value(DigestUtils.md5Hex(md5Source));
            sysConfigMap.put(sysConfig.getConfigKey(), sysConfig);
        }
        LOGGER.debug("获取全量配置信息--{}", sysConfigs.toString());
        return sysConfigMap;
    }

}
