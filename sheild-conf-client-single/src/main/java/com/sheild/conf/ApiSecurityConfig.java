//package com.hispeed.development;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//
///**
// * @author snowalker
// * @date 2018/5/24
// * @desc 接口安全spring security配置
// */
//@Configuration
//@EnableWebSecurity
////@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService());
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        /**创建用户账户密码*/
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withUsername("config").password("config12345").roles("ADMIN").build());
//        System.out.println("[sheild-conf-auth]后台管理页面账户名:config,密码:config12345!");
//        return manager;
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        System.out.println("111111111111111111111111111");
//        // 关闭csrf保护功能（跨域访问）
//        http.csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/configure").hasAuthority()
//                .antMatchers("/configure").permitAll();//访问API下无需登录认证权限
//    }
//}
