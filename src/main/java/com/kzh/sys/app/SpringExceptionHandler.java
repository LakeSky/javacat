package com.kzh.sys.app;

import org.hibernate.TypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by gang on 2018/7/17.
 */
public class SpringExceptionHandler extends DefaultHandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView mv = new ModelAndView("/error/errorPage");
        mv.addObject("ex", ex);
        ex.printStackTrace();

        if (ex instanceof TypeMismatchException) {
            mv.setViewName("/error/errorPage");
            return mv;
        }else
        if (ex instanceof Exception) {
            //错误已交给其他处理器处理
            return null;
        }
        return mv;
    }
}
