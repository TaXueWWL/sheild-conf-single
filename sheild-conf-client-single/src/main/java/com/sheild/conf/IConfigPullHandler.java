package com.sheild.conf;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author wuwl@19pay.com.cn
 * @date 2018-4-17
 * @desc 配置项被观察者接口，定义对配置项的更新策略
 */
interface IConfigPullHandler {

//    void checkConfig(ScheduledExecutorService exec,
//                     Runnable command,
//                     long initialDelay,
//                     long delay,
//                     TimeUnit unit);

    void runExec();
}
