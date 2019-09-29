package com.kzh.sys.service.sys.security;

import com.kzh.sys.service.sys.RoleResourceService;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import java.util.Collection;
import java.util.Map;


public class MySecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    public static Map<String, Collection<ConfigAttribute>> resourceMap = null;

    public static Map<String, Collection<ConfigAttribute>> getResourceMap() {
        return resourceMap;
    }

    public Collection<ConfigAttribute> getAllConfigAttributes() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean supports(Class<?> clazz) {
        // TODO Auto-generated method stub
        return true;
    }

    private void loadResourceDefine() {
        if (resourceMap == null || resourceMap.size() == 0) {
            resourceMap = RoleResourceService.getResourceAuthority();
        }
    }

    //这个地方是实现spring的security的关键点
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        String url = ((FilterInvocation) object).getRequest().getRequestURI();
        String contextPath = ((FilterInvocation) object).getRequest().getContextPath();
        if (contextPath != null && contextPath.length() > 0) {
            url = url.substring(contextPath.length());
        }
        loadResourceDefine();
        return resourceMap.get(url);
    }
}