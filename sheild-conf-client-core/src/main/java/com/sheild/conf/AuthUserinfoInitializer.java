//package com.sheild.conf;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//
///**
// * @author snowalker
// * @date 2018/5/30
// * @desc 用户登录信息初始化组件
// * 系统初始化时，执行一次该逻辑插入表中信息若表中已经存在则不插入
// */
//@Component
//public class AuthUserinfoInitializer {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(AuthUserinfoInitializer.class);
//
//    @Autowired
//    ConfigRepository configRepository;
//
//    @PostConstruct
//    private void init() {
//        UserAuthEntity userAuthEntity = configRepository.querySheildConfUserAuthEntity();
//        if (userAuthEntity == null) {
//            UserAuthEntity authEntity = new UserAuthEntity();
//            authEntity.setUserName("admin");
//            authEntity.setUserPassword("123456");
//            if (configRepository.insertUserAuthEntity(authEntity)) {
//                LOGGER.info("sheild-conf管理员账户新增完毕，请及时修改密码！账户名:" + authEntity.getUserName() + ",密码:" +
//                        authEntity.getUserPassword());
//            }
//        } else {
//            LOGGER.info("sheild-conf管理员账户已存在，账户名:" + userAuthEntity.getUserName() + ",密码:" +
//                    userAuthEntity.getUserPassword());
//        }
//    }
//
//    /**
//     * 用户登录信息实体
//     */
//    protected static class UserAuthEntity {
//
//        private String userName;
//        private String userPassword;
//
//        public String getUserName() {
//            return userName;
//        }
//
//        public void setUserName(String userName) {
//            this.userName = userName;
//        }
//
//        public String getUserPassword() {
//            return userPassword;
//        }
//
//        public void setUserPassword(String userPassword) {
//            this.userPassword = userPassword;
//        }
//    }
//}
