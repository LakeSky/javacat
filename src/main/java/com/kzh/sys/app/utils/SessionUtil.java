package com.kzh.sys.app.utils;

import com.kzh.sys.model.Role;
import com.kzh.sys.model.User;
import com.kzh.sys.util.SysUtil;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.HashMap;
import java.util.Map;

/**
 * 禁止乱加存储的内容
 * Created with IntelliJ IDEA.
 * kzh
 * Date: 2014/05/15
 * Time: 上午11:05
 */
public class SessionUtil {
    private static final Logger logger = Logger.getLogger(SessionUtil.class);
    public static final String LoginIp = "_LoginIp";
    public static final String UserName = "_UserName";
    public static final String _User = "_User";

    private static final ThreadLocal<Map<String, Object>> threadContext = new ThreadLocal<Map<String, Object>>() {
        @Override
        protected Map<String, Object> initialValue() {
            return new HashMap<>();
        }
    };

    private static void put(String key, Object value) {
        getContextMap().put(key, value);
    }

    private static Object remove(String key) {
        return getContextMap().remove(key);
    }

    private static Object get(String key) {
        return getContextMap().get(key);
    }

    /**
     * 取得thread context Map的实例。
     *
     * @return thread context Map的实例
     */
    private static Map<String, Object> getContextMap() {
        return threadContext.get();
    }


    public static void setCurrentUserName(String userName) {
        put(UserName, userName);
    }

    public static void setCurrentUser(User user) {
        put(_User, user);
    }

    public static void setCurrentLoginIp(String loginIp) {
        put(LoginIp, loginIp);
    }

    public static User getUser() {
        User user = (User) get(_User);
        if (user == null && SecurityContextHolder.getContext().getAuthentication() != null) {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return user;
    }

    public static Role getRole() {
        return getUser().getRole();
    }

    public static String getCurrentLoginIp() {
        return SysUtil.objectToString(get(LoginIp));
    }

    public static void clear() {
        threadContext.set(null);
        threadContext.remove();
    }

    public static String getUserName() {
        String apiUserName = SysUtil.objectToString(get(UserName));
        if (SysUtil.isNotEmpty(apiUserName)) {
            return apiUserName;
        }
        String username = "";
        try {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                return "";
            }
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof String) {
                if ("anonymousUser".equals(principal)) {
                    logger.info("匿名访问");
                }
                return username;
            }
            UserDetails userDetails = (UserDetails) principal;
            if (userDetails != null) {
                username = userDetails.getUsername();
            }
            return username;
        } catch (Exception e) {
            logger.error("用户未登陆", e);
        }
        return username;
    }

    public static boolean hasRole(String roles) {
        try {
            if (SysUtil.isEmpty(roles)) {
                return false;
            }
            String[] roleKeys = roles.split(",");
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            for (GrantedAuthority ga : userDetails.getAuthorities()) {
                for (String roleKey : roleKeys) {
                    if (roleKey.equals(ga.getAuthority())) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getUserIp() {
        String ip = "";
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            WebAuthenticationDetails wauth = (WebAuthenticationDetails) auth.getDetails();
            ip = wauth.getRemoteAddress();
            System.out.println("当前登录用户的ip：" + wauth.getRemoteAddress());
            //System.out.println("当前登录用户的sessionID：" + wauth.getSessionId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ip;
    }
}
