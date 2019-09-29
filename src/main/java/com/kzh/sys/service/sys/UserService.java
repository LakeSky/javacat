package com.kzh.sys.service.sys;

import com.kzh.busi.enums.BaseRole;
import com.kzh.busi.enums.DataLevel;
import com.kzh.sys.app.AppConstant;
import com.kzh.sys.app.utils.AuthorityUtil;
import com.kzh.sys.app.utils.SessionUtil;
import com.kzh.sys.core.dao.GenericRepository;
import com.kzh.sys.core.dao.Restrictions;
import com.kzh.sys.core.dao.SimpleExpression;
import com.kzh.sys.core.exception.WorldValidateException;
import com.kzh.sys.dao.RoleDao;
import com.kzh.sys.dao.RoleMenuDao;
import com.kzh.sys.dao.UserDao;
import com.kzh.sys.enums.Platform;
import com.kzh.sys.model.Role;
import com.kzh.sys.model.RoleMenu;
import com.kzh.sys.model.User;
import com.kzh.sys.pojo.Result;
import com.kzh.sys.pojo.WorldPage;
import com.kzh.sys.util.AES;
import com.kzh.sys.util.CollectionUtil;
import com.kzh.sys.util.SysUtil;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
public class UserService extends BaseService<User> {
    private static final Logger logger = Logger.getLogger(UserService.class);

    public static Map<String, User> usernameUserMap = new ConcurrentHashMap<>();

    @Resource
    private UserDao userDao;
    @Resource
    private RoleDao roleDao;
    @Resource
    private RoleMenuDao roleMenuDao;
    @Resource
    private TokenService tokenInfoService;
    @Resource
    private RoleService roleService;

    @Override
    public GenericRepository getDao() {
        return userDao;
    }

    public Page<User> findPage(User entity, WorldPage worldPage) throws Exception {
        User user = getByUsername(SessionUtil.getUserName());
        List<SimpleExpression> expressions = entity.initExpressions();
        expressions.add(Restrictions.eq("delFlag", false, true));
        if (!AuthorityUtil.hasRole(BaseRole.ROLE_ROOT)) {
            expressions.add(Restrictions.ne("username", AppConstant.USERNAME_ADMIN, true));
            expressions.addAll(dataExpressions(user));
        }
        expressions.add(Restrictions.ne("id", user.getId(), true));
        if (entity.getRole() != null && SysUtil.isNotEmpty(entity.getRole().getId())) {
            expressions.add(Restrictions.eq("role.id", entity.getRole().getId(), true));
        }
        Page<User> page = userDao.findByPage(expressions, worldPage.getPageRequest());
        return page;
    }

    private List<SimpleExpression> dataExpressions(User user) {
        List<SimpleExpression> expressions = new ArrayList<>();
        switch (getDataLevel(user)) {
            case ALL:
                break;
            default:
                expressions.add(Restrictions.eq("id", "-1", true));
                break;
        }
        return expressions;
    }

    public Result login(String username, String password, Platform platform) {
        User user = userDao.findByUsername(username);
        password = AES.encrypt(password, AppConstant.encryptKey);
        if (user != null && user.getPassword().equals(password)) {
            String token = tokenInfoService.getToken(user.getId(), platform);
            return new Result(true, "成功", token);
        }
        return new Result(false, "用户名或密码不正确");
    }

    public User getByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public User save(User user) {
        User userDb = userDao.save(user);
        UserService.usernameUserMap.put(userDb.getUsername(), userDb);
        return userDb;
    }

    public Result saveUser(User entity) throws Exception {
        Result result = entity.validateField();
        if (!result.isSuccess()) {
            return result;
        }
        User tDb;
        Role role = roleDao.getOne(entity.getRole().getId());
        entity.setRole(role);
        if (SysUtil.isEmpty(entity.getId())) {
            //添加
            if (SysUtil.isEmpty(entity.getPassword())) {
                throw new WorldValidateException("密码不能为空");
            }
            User user = userDao.findByUsername(entity.getUsername());
            if (user != null) {
                return new Result(false, "用户名已经存在");
            }
            entity.setPassword(AES.encrypt(entity.getPassword(), AppConstant.encryptKey));
            tDb = save(entity);
        } else {
            //编辑
            User user = userDao.getOne(entity.getId());
            user.initModifyFields(entity);
            tDb = save(user);
        }
        return new Result(true, "成功", tDb);
    }

    public void del(String[] ids) {
        User curUser = getByUsername(SessionUtil.getUserName());
        List<User> users = userDao.findByIdIn(Arrays.asList(ids));
        if (CollectionUtil.isNotEmpty(users)) {
            for (User user : users) {
                if (AppConstant.USERNAME_ADMIN.equals(user.getUsername()) || AppConstant.USERNAME_PLATFORM.equals(user.getUsername())) {
                    throw new WorldValidateException("系统用户不可删除");
                }
                if (curUser.getId().equals(user.getId())) {
                    throw new WorldValidateException("无法删除自己");
                }
            }
            userDao.delete(users);
            for (User user : users) {
                UserService.usernameUserMap.remove(user.getUsername());
            }
        }
    }

    public Result modifyPassword(String username, String oldPassword, String newPassword, String rePassword) {
        User user = userDao.findByUsername(username);
        if (user == null) {
            return new Result(false, "用户不存在");
        }
        if (SysUtil.isEmpty(newPassword)) {
            return new Result(false, "新密码不能为空");
        }
        if (!newPassword.equals(rePassword)) {
            return new Result(false, "两次书写的密码不一致");
        }
        if (!user.getPassword().equals(AES.encrypt(oldPassword, AppConstant.encryptKey))) {
            return new Result(false, "原密码错误");
        } else {
            user.setPassword(AES.encrypt(newPassword, AppConstant.encryptKey));
            save(user);
        }

        return new Result(true, "密码修改成功");
    }

    public void initUserCache() {
        List<User> users = userDao.findAll();
        if (CollectionUtil.isNotEmpty(users)) {
            for (User user : users) {
                usernameUserMap.put(user.getUsername(), user);
            }
        }
    }

    public Result getUserRoles(String id) {
        User user = userDao.getOne(id);
        List<Map<String, String>> roleMapList = new ArrayList<>();
        List<Role> roles = roleService.getCanSelect();
        if (CollectionUtil.isNotEmpty(roles)) {
            for (Role role : roles) {
                Map<String, String> map = new HashMap<>();
                map.put("id", role.getId());
                if (user.getRole() != null && role.getId().equals(user.getRole().getId())) {
                    map.put("checked", "true");
                } else {
                    map.put("checked", "false");
                }
                map.put("roleKey", role.getRoleKey());
                map.put("roleName", role.getRoleName());
                roleMapList.add(map);
            }
        }

        return new Result(true, "", roleMapList);
    }

    public void saveAvatar(String username, String avatarUrl) {
        User user = userDao.findByUsername(username);
        user.setAvatar(avatarUrl);
        save(user);
    }

    //获取一个用户拥有的所有菜单的id集合
    public Set<String> getUserMenuIds(String username) {
        Set<String> menuIds = new HashSet<>();
        User user = userDao.findByUsername(username);
        if (user.getRole() != null) {
            List<RoleMenu> roleMenus = roleMenuDao.findByRoleId(user.getRole().getId());
            if (CollectionUtil.isNotEmpty(roleMenus)) {
                for (RoleMenu roleMenu : roleMenus) {
                    menuIds.add(roleMenu.getMenuId());
                }
            }
        }
        return menuIds;
    }

    //获取一个用户的数据权限，数据权限定义在角色上
    public DataLevel getDataLevel(User user) {
        DataLevel dataLevel = DataLevel.ME;
        if (user.getRole() != null) {
            return user.getRole().getDataLevel();
        } else {
            logger.error("用户未配置角色");
        }

        return dataLevel;
    }

    public void saveRole(String userId, String roleId) {
        Role role = roleDao.getOne(roleId);
        User user = userDao.getOne(userId);
        user.setRole(role);
        save(user);
    }
}
