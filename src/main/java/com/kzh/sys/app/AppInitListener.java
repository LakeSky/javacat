package com.kzh.sys.app;

import com.kzh.sys.app.utils.AppUtils;
import com.kzh.sys.service.sys.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

/**
 * Created by gang on 2016/12/22.
 */
//web应用启动的时候执行初始化的地方(这个地方能够使用spring的依赖注入将service注入到这里可以帮助我们进行缓存以及配置的初始化)
@Component
public class AppInitListener implements InitializingBean, ServletContextAware {
    @Resource
    private ConfigService sysConfigService;
    @Resource
    private TokenService tokenInfoService;
    @Resource
    private MenuService menuService;
    @Resource
    private RoleService roleService;
    @Resource
    private UserService userService;
    @Resource
    private RoleMenuService roleMenuService;
    @Resource
    private RoleResourceService roleResourceService;

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        //在这个地方初始化缓存信息
        AppConstant.initPath();
        AppService.initCache();
        sysConfigService.initConfigCache();
        userService.initUserCache();
        tokenInfoService.initCache();
        menuService.initMenu();
        roleService.initRoleCache();
        roleService.initBaseRole();
        roleMenuService.initRoleMenuCache();
        roleResourceService.initCache();


        AppConstant.defaultRelatedMenuMap = AppUtils.getDefaultRelatedUrl();
    }
}
