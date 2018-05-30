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

    /**
     * 获取当前应用所有配置项
     * @return
     */
    protected List<SysConfig> getAllConfigs() {
        String sql = SQL.SQL_FETCH_ALL_CONFIGS;
        final List<SysConfig> sysConfigs = new CopyOnWriteArrayList<>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                SysConfig sysConfig = new SysConfig();
                sysConfig.setConfigId(rs.getInt("configId"))
                        .setConfigKey(rs.getString("configKey"))
                        .setConfigValue(rs.getString("configValue"))
                        .setConfigDesc(rs.getString("configDesc"))
                        .setProjectName(rs.getString("projectName"))
                        .setOptUser(rs.getString("optUser"))
                        .setInsertTime(rs.getString("insertTime"))
                        .setUpdateTime(rs.getString("updateTime"))
                        .setConfigSwitch(rs.getInt("configSwitch"));
                sysConfigs.add(sysConfig);
            }
        });
        return sysConfigs;
    }

    /**
     * 新增配置项
     * @param sysConfig
     * @return
     */
    protected boolean addOneSysConfig(SysConfig sysConfig) {
        String sql = SQL.INSERT_NEW_SYSCONFIG;
        int count = jdbcTemplate.update(sql, new Object[]{
                sysConfig.getConfigKey(),
                sysConfig.getConfigValue(),
                sysConfig.getConfigDesc(),
                sysConfig.getOptUser(),
                sysConfig.getProjectName()
        });
        if (count == 1) {
            return true;
        }
        return false;
    }

    /**
     * 启用配置项
     * @param configId
     * @return
     */
    protected boolean enableConfig(int configId) {
        String sql = SQL.ENABLE_CONFIG;
        int count = jdbcTemplate.update(sql, new Object[]{configId});
        if (count == 1) {
            return true;
        }
        return false;
    }

    /**
     * 禁用配置项
     * @param configId
     * @return
     */
    protected boolean disableConfig(int configId) {
        String sql = SQL.DISABLE_CONFIG;
        int count = jdbcTemplate.update(sql, new Object[]{configId});
        if (count == 1) {
            return true;
        }
        return false;
    }

    /**
     * 新增用户登录后台账户密码信息
     * @param userAuthEntity
     * @return
     */
    protected boolean insertUserAuthEntity(AuthUserinfoInitializer.UserAuthEntity userAuthEntity) {
        String sql = SQL.INSERT_NEW_USER_AUTH_JSON_STR;
        int count = jdbcTemplate.update(sql, new Object[]{JSON.toJSONString(userAuthEntity)});
        if (count == 1) {
            return true;
        }
        return false;
    }


    /**
     * 查询用户登录管理页面账户信息
     * @return
     */
    protected AuthUserinfoInitializer.UserAuthEntity querySheildConfUserAuthEntity() {
        String sql = SQL.QUERY_USER_AUTH_INFO;
        final StringBuffer authJson = new StringBuffer();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                authJson.append(rs.getString("CONFIG_VALUE"));
            }
        });
        AuthUserinfoInitializer.UserAuthEntity userAuthEntity = null;
        if (authJson.toString().length() > 0) {
            userAuthEntity = new Gson().fromJson(authJson.toString(), AuthUserinfoInitializer.UserAuthEntity.class);
        }
        return userAuthEntity;
    }

    /**
     * 更新配置
     * @param sysConfig
     * @return
     */
    protected boolean updateSysConfig(SysConfig sysConfig) {
        String sql = SQL.UPDATE_CONFIG;
        if (StringUtils.isEmpty(sysConfig.getOptUser())) {
            sysConfig.setOptUser("administrator");
        }
        if (StringUtils.isEmpty(sysConfig.getProjectName())) {
            sysConfig.setProjectName("common");
        }
        int count = jdbcTemplate.update(sql,
                new Object[] {
                        sysConfig.getConfigValue(),
                        sysConfig.getConfigDesc(),
                        sysConfig.getOptUser(),
                        sysConfig.getProjectName(),
                        sysConfig.getConfigId()
                });
        if (count == 1) {
            return true;
        }
        return false;
    }

    /**
     * 根据id查询对应的配置项
     * @param configId
     * @return
     */
    protected SysConfig getConfigById(String configId) {
        String sql = SQL.SQL_GET_CONFIG_BY_ID;
        final SysConfig config = new SysConfig();
        jdbcTemplate.query(sql, new Object[]{configId}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                config.setConfigId(rs.getInt("configId"))
                        .setConfigKey(rs.getString("configKey"))
                        .setConfigValue(rs.getString("configValue"))
                        .setConfigDesc(rs.getString("configDesc"))
                        .setProjectName(rs.getString("projectName"))
                        .setOptUser(rs.getString("optUser"))
                        .setInsertTime(rs.getString("insertTime"))
                        .setUpdateTime(rs.getString("updateTime"))
                        .setConfigSwitch(rs.getInt("configSwitch"));
            }
        });
        return config;
    }

}
