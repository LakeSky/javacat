package com.kzh.sys.service.sys;

import com.kzh.busi.enums.BaseRole;
import com.kzh.busi.enums.DataLevel;
import com.kzh.sys.app.utils.AuthorityUtil;
import com.kzh.sys.app.utils.SessionUtil;
import com.kzh.sys.core.dao.GenericRepository;
import com.kzh.sys.core.dao.Restrictions;
import com.kzh.sys.core.dao.SimpleExpression;
import com.kzh.sys.core.exception.WorldValidateException;
import com.kzh.sys.dao.RoleDao;
import com.kzh.sys.dao.RoleMenuDao;
import com.kzh.sys.dao.RoleResourceDao;
import com.kzh.sys.model.Role;
import com.kzh.sys.model.RoleMenu;
import com.kzh.sys.model.RoleResource;
import com.kzh.sys.model.User;
import com.kzh.sys.pojo.Result;
import com.kzh.sys.pojo.WorldPage;
import com.kzh.sys.util.CollectionUtil;
import com.kzh.sys.util.SysUtil;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
public class RoleService extends BaseService<Role> {
    private static final Logger logger = Logger.getLogger(RoleService.class);
    public static Map<String, Role> roleIdRoleMap = new ConcurrentHashMap<>();

    @Override
    public GenericRepository getDao() {
        return roleDao;
    }

    @Resource
    private RoleDao roleDao;
    @Resource
    private RoleResourceDao roleResourceDao;
    @Resource
    private RoleMenuDao roleMenuDao;
    @Resource
    private RoleMenuService roleMenuService;
    @Resource
    private UserService userService;
    @Resource
    private RoleResourceService roleResourceService;

    public Page<Role> page(Role entity, WorldPage worldPage) throws Exception {
        List<SimpleExpression> expressions = entity.initExpressions();
        expressions.add(Restrictions.eq("delFlag", false, true));
        User user = SessionUtil.getUser();
        if (!AuthorityUtil.hasRole(BaseRole.ROLE_ROOT)) {
            expressions.add(Restrictions.ne("roleKey", BaseRole.ROLE_ROOT, true));
            expressions.add(Restrictions.ne("id", user.getRole().getId(), true));

            //首先要计算登陆用户的最高角色的level
            DataLevel userDataLevel = userService.getDataLevel(user);
            List<DataLevel> dataLevels = DataLevel.getLteDataLevels(userDataLevel);
            expressions.add(Restrictions.in("dataLevel", dataLevels, true));
        }
        Page<Role> page = roleDao.findByPage(expressions, worldPage.getPageRequest());
        return page;
    }

    public List<Role> getCanSelect() {
        User user = SessionUtil.getUser();
        List<SimpleExpression> expressions = new ArrayList<>();
        if (!AuthorityUtil.hasRole(BaseRole.ROLE_ROOT)) {
            expressions.add(Restrictions.ne("roleKey", BaseRole.ROLE_ROOT, true));
            DataLevel userDataLevel = userService.getDataLevel(user);
            List<DataLevel> dataLevels = DataLevel.getLteDataLevels(userDataLevel);
            expressions.add(Restrictions.in("dataLevel", dataLevels, true));
        }
        expressions.add(Restrictions.ne("id", user.getRole().getId(), true));
        List<Role> roles = roleDao.findAll(expressions);
        return roles;
    }

    public void del(String[] ids) {
        Set<String> roleIds = CollectionUtil.arrayToSet(ids);
        List<SimpleExpression> expressions = new ArrayList<SimpleExpression>();
        expressions.add(Restrictions.in("id", roleIds, true));
        List<Role> roles = roleDao.findAll(expressions);
        if (CollectionUtil.isNotEmpty(roles)) {
            for (Role role : roles) {
                for (BaseRole baseRole : BaseRole.values()) {
                    if (baseRole.name().equals(role.getRoleKey())) {
                        throw new WorldValidateException("基础角色不能删除");
                    }
                }
            }
            roleDao.deleteInBatch(roles);
            for (Role role : roles) {
                roleIdRoleMap.remove(role.getId());
            }
        }
        //删除角色的时候要清理roleResource表和userRole表还有roleMenu
        List<SimpleExpression> expressionsRoleResource = new ArrayList<SimpleExpression>();
        expressionsRoleResource.add(Restrictions.in("roleId", roleIds, true));
        List<RoleResource> roleResources = roleResourceDao.findAll(expressionsRoleResource);
        roleResourceDao.delete(roleResources);

        List<SimpleExpression> expressionsRoleMenu = new ArrayList<SimpleExpression>();
        expressionsRoleMenu.add(Restrictions.in("roleId", roleIds, true));
        List<RoleMenu> roleMenus = roleMenuDao.findAll(expressionsRoleMenu);
        roleMenuDao.delete(roleMenus);
    }

    public Result saveRole(Role role) {
        if (SysUtil.isEmpty(role.getRoleName())) {
            return new Result(false, "角色名称不能为空!");
        }
        if (role.getDataLevel() == null) {
            return new Result(false, "数据权限不能为空!");
        }
        Role roleExist = null;
        if (SysUtil.isEmpty(role.getId())) {//添加
            roleExist = roleDao.findByRoleName(role.getRoleName());
            if (roleExist != null) {
                return new Result(false, "角色已经存在");
            }
            roleExist = new Role();
            roleExist.setRoleKey("ROLE_" + role.getRoleName());
            roleExist.setRoleName(role.getRoleName());
            roleExist.setDataLevel(role.getDataLevel());
            roleExist.setDeviceTypes(role.getDeviceTypes());
            roleExist = roleDao.save(roleExist);
        } else {//编辑
            roleExist = roleDao.getOne(role.getId());
            boolean ifBaseRole = false;
            for (BaseRole baseRole : BaseRole.values()) {
                if (baseRole.name().equals(roleExist.getRoleKey())) {
                    ifBaseRole = true;
                }
            }
            if (!ifBaseRole) {
                //基础角色不更改key
                roleExist.setRoleKey("ROLE_" + role.getRoleName());
            }
            roleExist.setRoleName(role.getRoleName());
            roleExist.setDataLevel(role.getDataLevel());
            roleExist.setDeviceTypes(role.getDeviceTypes());
            roleExist = roleDao.save(roleExist);
        }

        roleMenuService.updateRoleMenu(roleExist.getId(), role.getSelMenuIds());
        roleResourceService.saveRoleResource(roleExist.getId(), role.getSelSelResourceIds());
        roleIdRoleMap.put(roleExist.getId(), roleExist);
        return new Result(true, "成功", roleExist);
    }

    public Role getRole(String id) {
        return roleDao.findOne(id);
    }

    public void initBaseRole() {
        for (BaseRole baseRole : BaseRole.values()) {
            Role role = roleDao.findByRoleKey(baseRole.name());
            if (role == null) {
                role = new Role(baseRole.name(), baseRole.getName());
                Role o = roleDao.save(role);
                RoleService.roleIdRoleMap.put(o.getId(), o);
            }
        }
    }

    public void initRoleCache() {
        List<Role> os = roleDao.findAll();
        if (CollectionUtil.isNotEmpty(os)) {
            for (Role o : os) {
                RoleService.roleIdRoleMap.put(o.getId(), o);
            }
        }
    }

    //新建角色的时候可选择的可控数据范围，要根据用户的角色的最高的datalevel来确定
    public List<DataLevel> getCanSelDataLevels(String username) {
        User user = userService.getByUsername(username);
        DataLevel topDataLevel = userService.getDataLevel(user);
        List<DataLevel> dataLevels = new ArrayList<>();
        for (DataLevel dataLevel : DataLevel.values()) {
            if (dataLevel.getLevel() >= topDataLevel.getLevel()) {
                dataLevels.add(dataLevel);
            }
        }

        return dataLevels;
    }
}
