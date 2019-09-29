package com.kzh.sys.app.tag;

import com.kzh.sys.app.utils.AuthorityUtil;
import com.kzh.sys.app.utils.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.jstl.core.ConditionalTagSupport;


public abstract class AbstractPermissionTag extends ConditionalTagSupport {
    private static final Logger logger = LoggerFactory.getLogger(AbstractPermissionTag.class);
    private String permissions;

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    protected boolean condition() {
        String userName = SessionUtil.getUserName();
        boolean andOperation = true;
        String[] permissionArray;
        //或运算
        permissions = permissions.replace(" ", "");
        if (permissions.contains("||")) {
            andOperation = false;
            permissionArray = permissions.split("\\|\\|");
        } else {
            //与运算
            permissionArray = permissions.split("&&");
        }
        try {
            boolean hasPermission;
            for (String permission : permissionArray) {
                hasPermission = AuthorityUtil.hasPermission(userName, permission);
                if (andOperation) {
                    if (!hasPermission) return false;
                } else {
                    if (hasPermission) return true;
                }
            }
            return andOperation;
        } catch (Exception e) {
            logger.warn("权限信息：userName:{},privilege:{}.", new Object[]{userName, permissions});
            logger.error("权限校验异常:", e);
        }
        return false;
    }
}
