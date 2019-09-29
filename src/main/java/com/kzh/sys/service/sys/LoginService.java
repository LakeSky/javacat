package com.kzh.sys.service.sys;

import com.kzh.busi.enums.BaseRole;
import com.kzh.sys.app.AppConfigKey;
import com.kzh.sys.app.AppConstant;
import com.kzh.sys.dao.RoleDao;
import com.kzh.sys.dao.UserDao;
import com.kzh.sys.model.Role;
import com.kzh.sys.model.User;
import com.kzh.sys.pojo.Result;
import com.kzh.sys.util.AES;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

/**
 * Created by gang on 2019/5/13.
 */

@Service
@Transactional
public class LoginService {
    @Resource
    private UserService userService;
    @Resource
    private UserDao userDao;
    @Resource
    private RoleDao roleDao;

    public void getDomainUser(String username, String password) {
        User user = userService.getByUsername(username);
        if (user == null) {
            user = new User();
            Role role = roleDao.findByRoleKey(BaseRole.COMMON.name());
            user.setRole(role);
            user.setUsername(username);
            user.setPassword(AES.encrypt(password, AppConstant.encryptKey));
            user = userService.save(user);
        } else {
            user.setPassword(AES.encrypt(password, AppConstant.encryptKey));
        }
    }

    public Result checkDomain(String username, String password) {
        String host = ConfigService.getConfigValue(AppConfigKey.domain_server_ip);//AD域IP，必须填写正确
        String domain = ConfigService.getConfigValue(AppConfigKey.domain_postfix);//域名后缀，例.@noker.cn.com
        String port = "389"; //端口，一般默认389
        String url = "ldap://" + host + ":" + port;//固定写法
        String user = username.indexOf(domain) > 0 ? username : username + domain;//网上有别的方法，但是在我这儿都不好使，建议这么使用
        Hashtable env = new Hashtable();//实例化一个Env
        DirContext ctx = null;
        env.put(Context.SECURITY_AUTHENTICATION, "simple");//LDAP访问安全级别(none,simple,strong),一种模式，这么写就行
        env.put(Context.SECURITY_PRINCIPAL, user); //用户名
        env.put(Context.SECURITY_CREDENTIALS, password);//密码
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");// LDAP工厂类
        env.put(Context.PROVIDER_URL, url);//Url
        try {
            ctx = new InitialDirContext(env);// 初始化上下文
            System.out.println("身份验证成功!");
            return new Result(true, "身份验证成功!");
        } catch (javax.naming.AuthenticationException e) {
            System.out.println("身份验证失败!");
            e.printStackTrace();
            return new Result(false, "身份验证失败!");
        } catch (javax.naming.CommunicationException e) {
            System.out.println("AD域连接失败!");
            e.printStackTrace();
            return new Result(false, "AD域连接失败!");
        } catch (Exception e) {
            System.out.println("身份验证未知异常!");
            e.printStackTrace();
            return new Result(false, "身份验证未知异常!");
        } finally {
            if (null != ctx) {
                try {
                    ctx.close();
                    ctx = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
