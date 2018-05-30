package com.conf.test;

import com.sheild.conf.Config;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 单机版配置测试服务启动类
 */
@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {"com.sheild"})
public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext =
                SpringApplication.run(App.class, args);
        LOGGER.info("shop-portal-server启动完成......" + Config.get("snowalker"));
    }

    /**
     * @author wuwl@19pay.com.cn
     * @date 2017-3-17
     * @describe 优化tomcat线程数目
     */
    class MyTomcatConnectorCustomizer implements TomcatConnectorCustomizer {
        public void customize(Connector connector) {
            Http11NioProtocol protocol = (Http11NioProtocol) connector
                    .getProtocolHandler();
            // 设置最大连接数
            protocol.setMaxConnections(2000);
            // 设置最大线程数
            protocol.setMaxThreads(2000);
            protocol.setConnectionTimeout(30000);
        }
    }
}
