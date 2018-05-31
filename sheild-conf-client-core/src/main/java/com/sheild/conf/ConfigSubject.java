package com.sheild.conf;

import com.sheild.conf.domain.config.SysConfig;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author wuwl@19pay.com.cn
 * @date 2018-4-17
 * @desc 配置项被观察者
 */
@Component
public class ConfigSubject implements Observable, IConfigPullHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigSubject.class);

    // 待注册的观察者列表
    private List<Observer> observerList = new CopyOnWriteArrayList<>();

    private ScheduledExecutorService configExec;           // 配置定时调度线程池
    private long initialDelay;                             // 定时任务初始化延迟时间长度
    private long delay;                                    // 一次执行终止和下一次执行开始之间的延迟
    private TimeUnit unit;              // 使用毫秒
    private int poolSize;


    public ConfigSubject() {}

    public ConfigSubject(ScheduledExecutorService configExec, int poolSize, long initialDelay, long delay, TimeUnit unit) {
        this.configExec = configExec;
        this.poolSize = poolSize;
        this.delay = delay;
        this.initialDelay = initialDelay;
        this.unit = unit;
    }

    /**
     * 执行更新操作，发现变更后通知观察者
     */
    @Override
    public void runExec() {
        // 初始化后开始执行更新操作
        configExec.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        doWork();
                    }
                }, initialDelay, delay, unit);
    }


    /**
     * <p>一共有三种情况</p>
     * <p>1. 本地有远程没有，基于本地迭代
     *      需要同步远程，本地优先</p>
     * <p>2. 远程有本地没有，基于远程迭代
     *      直接同步本地</p>
     * <p>3. 本地和远程都有，基于远程迭代
     *      比较MD5进行更新操作</p>
     * <p>当然，存在一种情况，某个配置项不再需要使用，即修改了代码逻辑
     * 删除了该配置<br/>
     * 此时，如果加载应用，根据逻辑还是会获取到不需要的配置项，这时只需要
     * 重启应用，<br/>并在远程删除配置项。如果是集群，可以增加同步配置的时间，保证一致性
     * <br/>当然，我们允许应用中存在一定的配置冗余<p/>
     */
    public void doWork() {
        LOGGER.debug("开始定时获取全量配置定时任务");
        ConfigRepository configRepository = (ConfigRepository)SpringConfigTool.getBean("configRepository");
        Map<String, SysConfig> remoteConfigMap = configRepository.getConfigMap();
        LOGGER.debug("获取到应用的全量配置数量为:" + remoteConfigMap.size() + ",本地缓存中的配置数量为:"
                + ConfigHolder.getConfig().size());
        StringBuffer stringBuffer = new StringBuffer();
        // 根据数据库中迭代本地
        for (String remoteKey : remoteConfigMap.keySet()) {
            SysConfig localConfig = ConfigHolder.getConfig().get(remoteKey);
            if (ConfigHolder.getConfig().get(remoteKey) == null) {
                // 本地不存在配置项，新增配置
                notifyIfLocalConfigNotExist(remoteConfigMap, stringBuffer, remoteKey);
            } else if (!getMd5Value(localConfig.getProjectName(),
                    localConfig.getConfigKey(),
                    localConfig.getConfigValue(),
                    remoteConfigMap.get(remoteKey)).equals(remoteConfigMap.get(remoteKey).getMd5Value())) {
                // 相同key对应的配置项MD5比对不相同，更新配置项
                notifyObserverIfConfigInvalid(remoteConfigMap, stringBuffer, remoteKey);
            }
        }
        LOGGER.debug("应用配置文件更新完毕, 发生变更的配置项列表为:{}", stringBuffer.toString());
    }

    /**
     * 本地不存在配置项，通知ConfigDirectUpdateObserver新增配置
     * @param remoteConfigMap
     * @param stringBuffer
     * @param remoteKey
     */
    private void notifyIfLocalConfigNotExist(Map<String, SysConfig> remoteConfigMap, StringBuffer stringBuffer, String remoteKey) {
        LOGGER.debug("key={}为新增配置，直接更新本地配置", remoteKey);
        List<Object> configList = new CopyOnWriteArrayList<>();
        configList.add(remoteKey);
        configList.add(remoteConfigMap.get(remoteKey));
        // 定义配置更新观察者--根据key修改对应的value
        Observer configObserver = new ConfigDirectUpdateObserver();
        this.notifyObservers(configObserver, configList);
        stringBuffer.append(",key=" + remoteKey + ",");
    }

    /**
     * 相同key对应的配置项MD5比对不相同，通知ConfigMD5UpdateObserver更新配置项
     * @param remoteConfigMap
     * @param stringBuffer
     * @param remoteKey
     */
    private void notifyObserverIfConfigInvalid(Map<String, SysConfig> remoteConfigMap, StringBuffer stringBuffer, String remoteKey) {
        LOGGER.debug("key={}配置项为更新的配置项，直接更新本地配置", remoteKey);
        List<Object> configList = new CopyOnWriteArrayList<>();
        configList.add(remoteKey);
        configList.add(remoteConfigMap.get(remoteKey));
        Observer configObserver = new ConfigMD5UpdateObserver();
        this.notifyObservers(configObserver, configList);
        stringBuffer.append(",key=" + remoteKey + ",");
    }

    /**
     * 计算本地配置MD5 哈希值
     * @param projectName
     * @param configKey
     * @param configValue
     * @return
     */
    private static String getMd5Value(String projectName, String configKey, String configValue, SysConfig sysConfig) {
        String md5Value = DigestUtils.md5Hex(projectName + configKey + configValue);
        LOGGER.debug("configKey={}对应的本地MD5为--{}, 远程MD5为--{}", configKey, md5Value, sysConfig.getMd5Value());
        return md5Value;
    }

    @Override
    public void addObserver(Observer observer) {
        this.observerList.add(observer);
    }

    @Override
    public void deleteObserver(Observer observer) {
        this.observerList.remove(observer);
    }

    @Override
    public void notifyObservers(Observer observer, List<Object> context) {
        observer.update(context);
    }

}
