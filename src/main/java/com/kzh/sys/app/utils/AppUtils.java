package com.kzh.sys.app.utils;

import com.kzh.sys.app.AppConstant;
import com.kzh.sys.model.Config;
import com.kzh.sys.service.generate.auto.QMapping;
import com.kzh.sys.service.sys.AppService;
import com.kzh.sys.service.sys.ConfigService;
import com.kzh.sys.service.sys.MenuService;
import com.kzh.sys.service.sys.RoleResourceService;
import com.kzh.sys.util.CollectionUtil;
import com.kzh.sys.util.SysUtil;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gang on 2016/10/19.
 */
public class AppUtils {
    //同一个Controller下面点击某个页面时默认点亮的左侧菜单的url后缀
    public final static String default_related_url_fix = "/home";

    //一个controller底下有好多url这些url在展示的时候左侧的菜单需要点亮，
    // 1.首先看页面有没有QMapping(relatedUrl="")标识，如果有，就用relatedUrl
    // 2.然后看页面本身是否是菜单，如果是那么点亮的关联菜单就是自己
    // 3.如果都不是，将当前Controller下的/home页面作为默认的关联菜单点亮
    public static Map<String, String> getDefaultRelatedUrl() {
        Map<String, String> urlMap = new HashMap<>();
        List<Class> controllerClasses = AppService.controllers;
        if (CollectionUtil.isNotEmpty(controllerClasses)) {
            for (Class controllerClass : controllerClasses) {
                RequestMapping annotation = (RequestMapping) controllerClass.getAnnotation(RequestMapping.class);
                if (annotation != null && ArrayUtils.isNotEmpty(annotation.value())) {
                    String baseUrl = annotation.value()[0];
                    Method[] methods = controllerClass.getDeclaredMethods();
                    if (ArrayUtils.isNotEmpty(methods)) {
                        for (Method method : methods) {
                            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                            String fullUrl = "";
                            if (requestMapping != null && ArrayUtils.isNotEmpty(requestMapping.value())) {
                                fullUrl = baseUrl + requestMapping.value()[0];
                                //将权限的url与名字放到缓存里
                                if (ifControl(requestMapping.name())) {
                                    RoleResourceService.urlNameMap.put(fullUrl, requestMapping.name());
                                }
                            }
                            QMapping qMapping = method.getAnnotation(QMapping.class);
                            if (qMapping != null && SysUtil.isNotEmpty(qMapping.relatedUrl())) {
                                urlMap.put(fullUrl, qMapping.relatedUrl());
                            } else {
                                //如果url是菜单，那么应该点亮这个菜单
                                if (MenuService.urlMenuMap.containsKey(fullUrl)) {
                                    urlMap.put(fullUrl, fullUrl);
                                } else {
                                    urlMap.put(fullUrl, baseUrl + default_related_url_fix);
                                }
                            }
                        }
                    }
                }
            }
        }
        return urlMap;
    }

    //判断是否要管控这个资源(RequestMapping如果有name属性就表示需要加入控制，如果有name属性但是不想加入控制就在name属性值前面加个减号)
    //@RequestMapping(value = "/home") 不控制
    //@RequestMapping(value = "/home",name="列表") 控制
    //@RequestMapping(value = "/home",name="-列表") 不控制
    public static boolean ifControl(String name) {
        if (SysUtil.isNotEmpty(name) && !name.startsWith("-")) {
            return true;
        }
        return false;
    }

    public static String getConfigValue(String key) {
        Config config = ConfigService.configMap.get(key);
        if (config != null) {
            return config.getConfigValue();
        }
        return null;
    }

    public static boolean needPreventRepeat(String url) {
        String lowerCaseUrl = url.toLowerCase();
        for (String preventRepeatMethod : AppConstant.preventRepeatKeywords) {
            if (lowerCaseUrl.contains(preventRepeatMethod)) {
                return true;
            }
        }
        return false;
    }

    public static String getTomcatPath(HttpServletRequest request) {
        String path = request.getServletContext().getRealPath("/");
        if (path.contains("webapps")) {
            int webappsIndex = path.indexOf("webapps");
            String tomcatPath = path.substring(0, webappsIndex);
            return tomcatPath;
        } else if (path.contains("target")) {
            File file = new File(path);
            return file.getParent();
        }
        return null;
    }
}
