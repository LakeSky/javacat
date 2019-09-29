package com.kzh.sys.app;

import com.kzh.sys.app.utils.AppUtils;
import com.kzh.sys.app.utils.SessionUtil;
import com.kzh.sys.model.User;
import com.kzh.sys.service.sys.ApiService;
import com.kzh.sys.service.sys.TokenService;
import com.kzh.sys.service.sys.UserService;
import com.kzh.sys.util.ResponseUtil;
import com.kzh.sys.util.SysUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by gang on 2017/11/2.
 */
public class AppTokenInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AppTokenInterceptor.class);
    @Resource
    private ApiService apiService;
    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        return validateToken(httpServletRequest, httpServletResponse);
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    private boolean validateToken(HttpServletRequest request, HttpServletResponse response) {
        String tokenInParam = request.getParameter("token");
        String tokenInHeader = request.getHeader("token");
        if (SysUtil.isAllEmpty(tokenInHeader, tokenInParam)) {
            logger.info("======================require token is null");
            ResponseUtil.returnJson(request, response, "缺少token，无法验证");
            return false;
        }
        if (tokenInParam != null) {
            tokenInHeader = tokenInParam;
        }

        Claims claims = null;
        try {
            claims = TokenService.verifyToken(tokenInHeader);
        } catch (ExpiredJwtException expiredJwtException) {
            logger.error("token 已过期", expiredJwtException);
            ResponseUtil.returnJson(request, response, "token已过期");
            return false;
        } catch (Exception e) {
            logger.error("token 不合法", e);
            ResponseUtil.returnJson(request, response, "token不合法");
            return false;
        }
        if (claims == null) {
            ResponseUtil.returnJson(request, response, "token不合法");
            return false;
        }

        String tokenKey = claims.getId();
        String memToken = TokenService.tokenMap.get(tokenKey);
        if (!tokenInHeader.equals(memToken)) {
            logger.error("token 已失效");
            ResponseUtil.returnJson(request, response, "token 已失效");
            return false;
        }
        String url = request.getRequestURI();
        logger.info("==================apiUrl:" + url);
        String userId = tokenKey;
        if (tokenKey.contains(";")) {
            userId = tokenKey.split(";")[0];
        }
        boolean verifyFlag = apiService.verifyApiUrl(userId, url);
        if (!verifyFlag) {
            ResponseUtil.returnJson(request, response, "没有权限！");
            return false;
        }
        User user = userService.get(userId);
        if (user == null) {
            ResponseUtil.returnJson(request, response, "该用户不存在！");
            return false;
        }
        if (AppUtils.needPreventRepeat(request.getRequestURI())) {
            boolean repeatLock = AppLock.lockSubmit(SessionUtil.getUserName(), request.getRequestURI(), 300);
            if (repeatLock) {
                ResponseUtil.returnJson(request, response, "重复提交！");
                return false;
            }
        }
        SessionUtil.setCurrentUserName(user.getUsername());
        SessionUtil.setCurrentUser(user);
        return true;
    }
}
