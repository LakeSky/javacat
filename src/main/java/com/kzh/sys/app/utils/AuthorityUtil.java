package com.kzh.sys.app.utils;

import com.kzh.busi.enums.BaseRole;
import com.kzh.sys.service.sys.RoleResourceService;
import com.kzh.sys.service.sys.security.MySecurityMetadataSource;
import com.kzh.sys.util.SysUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Iterator;

/**
 * URL请求资源判断
 */
public class AuthorityUtil {
    private static final Logger logger = LoggerFactory.getLogger(AuthorityUtil.class);

    //判断用户对url是否有权限
    public static boolean hasPermission(String userName, String url) {
        //未登陆状态直接返回false
        if (SysUtil.isEmpty(userName)) {
            return false;
        }
        if (SysUtil.isEmpty(url)) {
            return true;
        }
        boolean hasPermission = false;
        Collection<ConfigAttribute> configAttributes = MySecurityMetadataSource.resourceMap.get(url);
        if (configAttributes == null) {
            //属于被控制的资源，但是没有角色选择这个资源，默认便是谁也没有这个资源的权限
            if (RoleResourceService.urlNameMap.containsKey(url)) {
                return false;
            }
            return true;
        }
        Iterator<ConfigAttribute> iterator = configAttributes.iterator();
        while (iterator.hasNext()) {
            ConfigAttribute configAttribute = iterator.next();
            String needPermission = configAttribute.getAttribute();
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            for (GrantedAuthority ga : userDetails.getAuthorities()) {
                if (needPermission.equals(ga.getAuthority())) {
                    return true;
                }
            }
        }
        /*if (!hasThisPermission) {
            logger.debug("[userName:{}] doesn't have the request permission of  \"{}\"", userName, privilegeValue);
        } else {
            logger.debug("[userName:{}] has the request permission of  \"{}\"", userName, privilegeValue);
        }*/
        return hasPermission;
    }

    public static boolean hasRole(BaseRole baseRole) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        for (GrantedAuthority ga : userDetails.getAuthorities()) {
            if (baseRole.name().equals(ga.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}
