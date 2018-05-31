package com.sheild.conf;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author wuwl@19pay.com.cn
 * @date 2018-4-17
 * @desc 配置服务更新启动器
 */
@Component
public class ConfigCommandLineRunner implements CommandLineRunner {

    static ScheduledExecutorService configExec;

    @Override
    public void run(String... strings) throws Exception {
        // 新建线程池
        configExec = Executors.newScheduledThreadPool(10);
        // 定义配置更新被观察者
        ConfigSubject configSubject = new ConfigSubject(configExec, 30, 0, 10, TimeUnit.SECONDS);
        configSubject.runExec();
    }

}
