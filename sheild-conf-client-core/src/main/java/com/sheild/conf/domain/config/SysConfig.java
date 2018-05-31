package com.sheild.conf.domain.config;

import java.io.Serializable;

/**
 * @author wuwl@19pay.com.cn
 * @date 2018-4-2
 * @desc 配置实体
 * 更新策略：对工程id+模块id+配置key+配置value进行MD5，如果有变更则更新客户端
 */
public class SysConfig implements Serializable {

    private static final long serialVersionUID = 7173154101565448135L;

    private Integer configId;
    private String configKey;
    private String configValue;
    private String configDesc;
    private Integer projectId;
    private String projectName;
    private Integer moduleId;
    private String moduleName;
    private Integer configSwitch;               // 配置开关，默认为1关闭  0-开启，1-关闭
    private String optUser;                     // 操作人员，默认administrator
    private String insertTime;
    private String updateTime;
    private String md5Value;                    // md5值，用于版本控制
    private String configSwitchDesc;            // 配置标记描述

    public String getConfigSwitchDesc() {
        return configSwitchDesc;
    }

    public SysConfig setConfigSwitchDesc(String configSwitchDesc) {
        this.configSwitchDesc = configSwitchDesc;
        return this;
    }

    public SysConfig setMd5Value(String md5Value) {
        this.md5Value = md5Value;
        return this;
    }

    public String getMd5Value() {
        return md5Value;
    }

    public Integer getConfigId() {
        return configId;
    }

    public SysConfig setConfigId(Integer configId) {
        this.configId = configId;
        return this;
    }

    public String getConfigKey() {
        return configKey;
    }

    public SysConfig setConfigKey(String configKey) {
        this.configKey = configKey;
        return this;
    }

    public String getConfigValue() {
        return configValue;
    }

    public SysConfig setConfigValue(String configValue) {
        this.configValue = configValue;
        return this;
    }

    public String getConfigDesc() {
        return configDesc;
    }

    public SysConfig setConfigDesc(String configDesc) {
        this.configDesc = configDesc;
        return this;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public SysConfig setProjectId(Integer projectId) {
        this.projectId = projectId;
        return this;
    }

    public String getProjectName() {
        return projectName;
    }

    public SysConfig setProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public SysConfig setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
        return this;
    }

    public String getModuleName() {
        return moduleName;
    }

    public SysConfig setModuleName(String moduleName) {
        this.moduleName = moduleName;
        return this;
    }

    public Integer getConfigSwitch() {
        return configSwitch;
    }

    public SysConfig setConfigSwitch(Integer configSwitch) {
        this.configSwitch = configSwitch;
        return this;
    }

    public String getOptUser() {
        return optUser;
    }

    public SysConfig setOptUser(String optUser) {
        this.optUser = optUser;
        return this;
    }

    public String getInsertTime() {
        return insertTime;
    }

    public SysConfig setInsertTime(String insertTime) {
        this.insertTime = insertTime;
        return this;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public SysConfig setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    public String toString() {
        return "SysConfig{" +
                "configId=" + configId +
                ", configKey='" + configKey + '\'' +
                ", configValue='" + configValue + '\'' +
                ", configDesc='" + configDesc + '\'' +
                ", projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                ", moduleId=" + moduleId +
                ", moduleName='" + moduleName + '\'' +
                ", configSwitch=" + configSwitch +
                ", optUser='" + optUser + '\'' +
                ", insertTime='" + insertTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", md5Value='" + md5Value + '\'' +
                '}';
    }
}
