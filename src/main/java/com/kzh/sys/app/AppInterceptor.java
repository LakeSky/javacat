package com.kzh.sys.app;

import com.kzh.sys.app.utils.AppUtils;
import com.kzh.sys.app.utils.SessionUtil;
import com.kzh.sys.pojo.tree.BaseTreeNode;
import com.kzh.sys.service.sys.MenuService;
import com.kzh.sys.util.ResponseUtil;
import com.kzh.sys.util.SysUtil;
import com.kzh.sys.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by gang on 2017/1/24.
 */
public class AppInterceptor extends HandlerInterceptorAdapter implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AppInterceptor.class);
    @Resource
    private MenuService menuService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (SysUtil.isDevMode()) {
            request.setAttribute("dev", true);
        }
        request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/");
        request.setAttribute("contextPath", request.getContextPath());
        String username = SessionUtil.getUserName();
        if (SysUtil.isNotEmpty(username)) {
            if (!WebUtil.isAjaxRequest(request)) {
                String url = request.getServletPath();
                logger.info(url);
                String activeUrl;
                if (MenuService.urlMenuMap.containsKey(url)) {
                    activeUrl = url;
                } else {
                    activeUrl = AppConstant.defaultRelatedMenuMap.get(url);
                }
                if (SysUtil.isNotEmpty(activeUrl) && MenuService.urlMenuMap.containsKey(activeUrl)) {
                    BaseTreeNode treeNode = menuService.getAceResourceTree(request.getSession(), activeUrl);
                    request.setAttribute("rootNode", treeNode);
                    request.setAttribute("nodeNav", MenuService.getMenuNav(activeUrl));
                }
            } else {
                if (AppUtils.needPreventRepeat(request.getRequestURI())) {
                    boolean repeatLock = AppLock.lockSubmit(username, request.getRequestURI(), 300);
                    if (!repeatLock) {
                        ResponseUtil.returnJson(request, response, "重复提交！");
                        return false;
                    }
                }
            }
        }
        //todo 可以在这里统一检查用户是否登录
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        SessionUtil.clear();
    }
}