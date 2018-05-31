package com.sheild.conf;

import com.sheild.conf.domain.config.SysConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author wuwl@19pay.com.cn
 * @date 2018-4-17
 * @desc 配置服务controller
 */
@Controller
public class ConfigController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigController.class);

    @Autowired
    ConfigUiRepository configRepository;
    /**
     * 设置刷新频率及线程池大小
     * @param request
     * @param response
     * @return pool-size=10&delay=10
     */
    @RequestMapping(value = "execute", method = {RequestMethod.GET, RequestMethod.POST})
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.debug("ConfigSubject配置被观察者接口开始执行......");
        String pool_size = request.getParameter("pool-size") == null ? "10" : request.getParameter("pool-size");
        String delay = request.getParameter("delay") == null ? "10" : request.getParameter("delay");
        ConfigCommandLineRunner.configExec.shutdown();
        if (ConfigCommandLineRunner.configExec.isShutdown()) {
            ConfigCommandLineRunner.configExec = Executors.newScheduledThreadPool(Integer.valueOf(pool_size));
            // 定义配置更新被观察者
            ConfigSubject configSubject = new ConfigSubject
                    (ConfigCommandLineRunner.configExec,
                            Integer.valueOf(pool_size),
                            0,
                            Integer.valueOf(delay),
                            TimeUnit.SECONDS);
            configSubject.runExec();

            LOGGER.debug("新配置: pool_size={}, delay={}秒设置完毕，执行新的同步操作", pool_size, delay);
            request.setAttribute("return", "配置刷新线程池建立完成-->pool_size="
                    + pool_size + ",delay=" + delay);
            return "configure";
        }
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setAttribute("return", "默认线程池尚未完全关闭，请稍等然后重试");
        return "redirect:/configure.html?authcode=" + request.getSession().getAttribute("sheild-conf-token");
    }


    /**
     * 配置页面路由
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "configure", method = {RequestMethod.GET})
    public String configure(HttpServletRequest request, HttpServletResponse response) {
        /**token校验*/
        String apiToken = (String) request.getSession().getAttribute("sheild-conf-token");
        if (apiToken == null) {
            LOGGER.debug("token校验失败,重定向至token授权登录页面");
            return "redirect:/shield-conf-auth.html";
        }
        String authCode = request.getParameter("authcode");
        if (StringUtils.isEmpty(authCode) || !authCode.equalsIgnoreCase(apiToken)) {
            return "redirect:/shield-conf-auth.html";
        }
        LOGGER.debug("进入配置页面......");
        /**加载所有配置项*/
        List<SysConfig> sysConfigs = configRepository.getAllConfigs();
        if (sysConfigs == null) {
            sysConfigs = new CopyOnWriteArrayList<>();
        }
        for (SysConfig sysConfig : sysConfigs) {
            if (sysConfig.getConfigSwitch().intValue() == 0) {
                sysConfig.setConfigSwitchDesc("启用");
            } else {
                sysConfig.setConfigSwitchDesc("禁用");
            }
        }
        request.setAttribute("sysConfigs", sysConfigs);
        request.setAttribute("authcode", authCode);
        return "configure";
    }

    /**
     * 登录路由，获取authcode
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "shield-conf-auth", method = {RequestMethod.GET})
    public String shield_conf_auth(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.debug("跳转至token授权登录页面");
        return "shield-conf-auth";
    }

    @RequestMapping(value = "/api/config/fetch-authcode", method = {RequestMethod.GET, RequestMethod.POST})
    public String fetchAuthcode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /**登录校验*/
        String userName = request.getParameter("sheildConfName");
        String userPassword = request.getParameter("sheildConfPassword");
        AuthUserinfoInitializer.UserAuthEntity userAuthEntity = configRepository.querySheildConfUserAuthEntity();
        if (userName.equals(userAuthEntity.getUserName()) && userPassword.equals(userAuthEntity.getUserPassword())) {
            LOGGER.debug("[api]开始进行authcode获取逻辑");
            HttpSession session = request.getSession();
            String authcode = UUID.randomUUID().toString();
            LOGGER.debug("当前authcode值为:" + authcode);
            session.setAttribute("sheild-conf-token", authcode);
            response.getWriter().write("<html><head><title>auth-code</title></head><body><h2>[sheild-conf-single-client] authcode is: " + authcode + "</h2></body></html>");
            return null;
        }
        return "redirect:/401.html";
    }

    /**
     * 配置修改页面路由
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "update-sysconfig", method = {RequestMethod.GET})
    public String updateConfigPage(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam(value = "config-id", defaultValue = "0") String configfId) {
        /**token校验*/
        String apiToken = (String) request.getSession().getAttribute("sheild-conf-token");
        if (apiToken == null) {
            LOGGER.debug("token校验失败,重定向至token授权登录页面");
            return "redirect:/shield-conf-auth.html";
        }
        String authCode = request.getParameter("authcode");
        if (StringUtils.isEmpty(authCode) || !authCode.equalsIgnoreCase(apiToken)) {
            return "redirect:/shield-conf-auth.html";
        }
        LOGGER.debug("进入配置修改页面......configId={},sessionId={}", configfId, request.getSession().getId());
        /**获取id对应配置项*/
        SysConfig sysConfig = configRepository.getConfigById(configfId);
        LOGGER.debug("当前获取到的配置项详情为:{}", sysConfig.toString());
        request.setAttribute("sysConfig", sysConfig);
        return "update-sysconfig";
    }

    /**
     * 禁用配置
     * @param configId
     * @param response
     * @return
     */
    @RequestMapping(value = "/api/config/disable-config", method = {RequestMethod.GET})
    public String disableConfigAction(@RequestParam(value = "config-id", defaultValue = "0") Integer configId,
                                      HttpServletResponse response, HttpServletRequest request) {
        LOGGER.debug("进入配置禁用action，要禁用的配置config-id={}", configId);
        if (configRepository.disableConfig(configId)) {
            return "redirect:/configure.html?authcode=" + request.getSession().getAttribute("sheild-conf-token");
        }
        response.setStatus(500);
        return "redirect:/error.html";
    }

    /**
     * 启用配置
     * @param configId
     * @param response
     * @return
     */
    @RequestMapping(value = "/api/config/enable-config", method = {RequestMethod.GET})
    public String enableConfig(@RequestParam(value = "config-id", defaultValue = "0") Integer configId,
                                      HttpServletResponse response, HttpServletRequest request) {
        LOGGER.debug("进入配置启用action，启用的配置config-id={}", configId);
        if (configRepository.enableConfig(configId)) {
            return "redirect:/configure.html?authcode=" + request.getSession().getAttribute("sheild-conf-token");
        }
        response.setStatus(500);
        return "redirect:/error.html";
    }

    /**
     * 新增配置
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "api/config/add-new-config", method = {RequestMethod.POST})
    public String addNewConfig(HttpServletRequest request, HttpServletResponse response) {

        LOGGER.info("进入配置项新增action,sessionId=" + request.getSession().getId());
        String configKey = request.getParameter("configKey");
        String configValue = request.getParameter("configValue");
        String configDesc = request.getParameter("configDesc");
        String optUser = request.getParameter("configOptUser");
        String projectName = request.getParameter("projectName");
        if (StringUtils.isEmpty(configKey) || StringUtils.isEmpty(configValue)
                || StringUtils.isEmpty(configDesc) || StringUtils.isEmpty(optUser) || StringUtils.isEmpty(projectName)) {
            return "redirect:/error.html";
        }
        /**调用数据库操作进行新配置持久化*/
        SysConfig config = new SysConfig();
        config.setConfigKey(configKey).setConfigValue(configValue).setConfigDesc(configDesc).setOptUser(optUser).setProjectName(projectName);
        if (configRepository.addOneSysConfig(config)) {
            LOGGER.info("配置项添加成功,sessionId={},配置内容={}", request.getSession().getId(), config.toString());
            return "redirect:/configure.html?authcode=" + request.getSession().getAttribute("sheild-conf-token");
        }
        response.setStatus(500);
        return "redirect:/error.html";
    }

    /**
     * 修改配置
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "api/config/update-config", method = {RequestMethod.POST})
    public String updateConfigDetail(HttpServletRequest request, HttpServletResponse response) {

        LOGGER.info("进入配置项修改action,sessionId=" + request.getSession().getId());
        String configValue = request.getParameter("configValue");
        String configDesc = request.getParameter("configDesc");
        String optUser = request.getParameter("optUser");
        String projectName = request.getParameter("project-name");
        String configId = request.getParameter("configId");
        if (StringUtils.isEmpty(configId) || StringUtils.isEmpty(configValue)
                || StringUtils.isEmpty(configDesc) || StringUtils.isEmpty(optUser) || StringUtils.isEmpty(projectName)) {
            LOGGER.debug("configId=" + configId + ",configValue=" + configValue + ",configDesc=" + configDesc
            + ",optUser=" + optUser + ",projectName=" + projectName);
            return "redirect:/error.html";
        }
        /**调用数据库操作进行新配置持久化*/
        SysConfig config = new SysConfig();
        config.setConfigId(Integer.valueOf(configId)).setConfigValue(configValue).setConfigDesc(configDesc).setOptUser(optUser).setProjectName(projectName);
        if (configRepository.updateSysConfig(config)) {
            LOGGER.info("配置项修改成功,sessionId={},配置内容={}", request.getSession().getId(), config.toString());
            return "redirect:/configure.html?authcode=" + request.getSession().getAttribute("sheild-conf-token");
        }
        response.setStatus(500);
        return "redirect:/error.html";
    }

    @RequestMapping(value = "401", method = {RequestMethod.GET})
    public String to401Page() {
        return "401";
    }

}
