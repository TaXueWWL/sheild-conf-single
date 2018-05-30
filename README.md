# sheild-conf-single分布式配置服务单机版本

>提供一站式配置管理服务

[分布式集群版本](https://github.com/TaXueWWL/shield-conf)

		
1. 建立数据库并导入deploy/sql中的SysConfig.sql脚本建立配置表
2. 构建项目sheild-conf-client-single，执行下方命令

                mvn clean install -DskipTests

4. 参考shield-demo-project-single进行开发。在pom中添加下方依赖

                <dependency>
                        <artifactId>shield-config-client-single</artifactId>
                        <groupId>com.hispeed.development</groupId>
                        <version>1.0.0</version>
                </dependency>

5. 对于springboot项目在启动类添加注解 @ComponentScan(basePackages = {"com.sheild"})，如下

                @SpringBootApplication
                @ComponentScan(basePackages = {"com.sheild"})
                public class App {
                        public static void main(String[] args) {
                                SpringApplication.run(App.class, args);
                        }
                        ......

6. 在需要调用配置的地方，使用 **Config.get("key")** 方法获取需要的配置，参数即为配置的key
7. 新增单机版管理功能，增加配置查询、新增、修改、配置项激活、禁用功能。下阶段计划增加账户体系，保证配置安全

        单机版配置页面url：ip:port/configure.html
        
8. 单机版配置管理页面新增授权码机制。应用启动时会在本地配置表中添加默认的用户账户名密码，默认为admin，12345
登陆后尽快修改为自己的用户名密码。不登录的情况下直接访问配置页面路由   **/configure.html** 会被强制重定向
到登录页面。输入用户名密码，获取到授权token，访问配置路由页面，拼接授权token，即可进入配置管理页面。

        url：ip:port/configure.html?authcode=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
   