package com.kzh.sys.app;

import com.kzh.sys.core.exception.WorldValidateException;
import com.kzh.sys.pojo.Result;
import com.kzh.sys.util.JsonUtil;
import com.kzh.sys.util.ResponseUtil;
import com.kzh.sys.util.WebUtil;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 平台异常信息跳转、解析
 */
public class AppExceptionResolver extends SimpleMappingExceptionResolver {
    private static final Logger logger = Logger.getLogger(AppExceptionResolver.class);

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        String currentPath = request.getRequestURI();
        String queryString = request.getQueryString();
        queryString = queryString == null ? "" : "?" + queryString;
        String url = currentPath + queryString;
        if (e instanceof WorldValidateException) {
            logger.error(url + "=====" + e.getMessage());
        } else {
            logger.error(url, e);
            //保存到mongdb
        }
        if (currentPath.startsWith("/api/")) {
            ModelAndView mv = new ModelAndView();
            ResponseUtil.returnJson(request, response, e.getMessage());
            return mv;
        }
        if (!WebUtil.isAjaxRequest(request)) {
            request.setAttribute("backUrl", url);
            return super.doResolveException(request, response, handler, e);
        }
        ModelAndView mv = new ModelAndView();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache, must-revalidate");
        try {
            if (e instanceof WorldValidateException) {
                Result result = new Result(false, e.getMessage());
                response.getWriter().write(JsonUtil.objectToJson(result));
            } else {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.getWriter().write(e.getMessage());
            }
        } catch (IOException ex) {
            e.printStackTrace();
        }
        return mv;
    }
}