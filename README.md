# sheild-conf-single分布式配置服务单机版本

>提供一站式配置管理服务

[分布式集群版本](https://github.com/TaXueWWL/shield-conf)


1. 新增单机版客户端配置，spring应用直接引入下方坐标

		<dependency>
            <artifactId>shield-config-client-single</artifactId>
            <groupId>com.hispeed.development</groupId>
            <version>1.3</version>
        </dependency>

2. 新增单机版管理功能，开发中，增加配置查询、新增、修改、配置项激活、禁用功能。下阶段计划增加账户体系，保证配置安全

        单机版配置页面url：ip:port/configure.html
        
3. 单机版配置管理页面新增授权码机制。应用启动时会在本地配置表中添加默认的用户账户名密码，默认为admin，12345
登陆后尽快修改为自己的用户名密码。不登录的情况下直接访问配置页面路由**/configure.html**会被强制重定向
到登录页面。输入用户名密码，获取到授权token，访问配置路由页面，拼接授权token，即可进入配置管理页面。

        url：ip:port/configure.html?authcode=xxxxxxxxxxxxxxxxxxxxxxxxx
   