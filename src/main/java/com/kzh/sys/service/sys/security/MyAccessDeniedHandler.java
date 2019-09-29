package com.kzh.sys.service.sys.security;

import com.kzh.sys.app.utils.SessionUtil;
import com.kzh.sys.pojo.Result;
import com.kzh.sys.util.JsonUtil;
import com.kzh.sys.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by gang on 2017/4/20.
 */
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    protected static final Logger logger = LoggerFactory.getLogger(MyAccessDeniedHandler.class);
    private String errorPage;

    public MyAccessDeniedHandler() {
    }

    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if (!response.isCommitted()) {
            if (!WebUtil.isAjaxRequest(request)) {
                logger.error("请求{}, SESSION_KEY: {},权限配置异常！", request.getRequestURI(), SessionUtil.getUserName());
                if (this.errorPage != null) {
                    request.setAttribute("SPRING_SECURITY_403_EXCEPTION", accessDeniedException);
                    response.setStatus(403);
                    RequestDispatcher dispatcher = request.getRequestDispatcher(this.errorPage);
                    dispatcher.forward(request, response);
                } else {
                    response.sendError(403, accessDeniedException.getMessage());
                }

                response.sendRedirect(request.getContextPath() + "/error/nopermission");
            } else {
                logger.error("Ajax请求{}, SESSION_KEY: {},权限配置异常！", request.getRequestURI(), SessionUtil.getUserName());
//                response.setContentType("text/html;charset=utf-8");
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setHeader("permissionstatus", "nopermission");
                Result result = new Result(false, "对不起，你的操作权限不足，请联系管理员!");
                response.getWriter().write(JsonUtil.objectToJson(result));
            }
        }
    }

    public void setErrorPage(String errorPage) {
        if (errorPage != null && !errorPage.startsWith("/")) {
            throw new IllegalArgumentException("errorPage must begin with '/'");
        } else {
            this.errorPage = errorPage;
        }
    }
}
