package com.sheild.conf;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 调用springboot容器上下文，取出bean实例
 */
@Component
public class SpringConfigTool implements ApplicationContextAware {

    @Autowired
    private static ApplicationContext context;
    private static volatile SpringConfigTool stools = null;

    public synchronized static SpringConfigTool init() {
        if (stools == null) {
            synchronized (SpringConfigTool.class) {
                if (stools == null) {
                    stools = new SpringConfigTool();
                    stools.setApplicationContext(context);
                }
            }
        }
        return stools;
    }

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        context = applicationContext;
    }

    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }

}


