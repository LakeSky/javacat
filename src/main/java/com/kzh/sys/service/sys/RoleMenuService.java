package com.kzh.sys.service.sys;

import com.kzh.busi.enums.BaseRole;
import com.kzh.sys.app.utils.AuthorityUtil;
import com.kzh.sys.dao.RoleMenuDao;
import com.kzh.sys.model.Menu;
import com.kzh.sys.model.Role;
import com.kzh.sys.model.RoleMenu;
import com.kzh.sys.pojo.tree.ZTreeNode;
import com.kzh.sys.service.sys.security.MySecurityMetadataSource;
import com.kzh.sys.util.CollectionUtil;
import com.kzh.sys.util.ListSortUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.Query;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by gang on 2017/2/3.
 */
@Service
@Transactional
public class RoleMenuService {
    private static final Logger logger = Logger.getLogger(RoleService.class);

    public static Map<String, RoleMenu> roleMenuMap = new ConcurrentHashMap<>();

    @Resource
    private UserService userService;
    @Resource
    private RoleMenuDao roleMenuDao;

    public ZTreeNode getRoleMenuTree(String username, String roleId) {
        Set<String> menusIds = userService.getUserMenuIds(username);
        Set<String> hasResourceIds = getMenuIds(roleId);
        Collection<Menu> menus = MenuService.menuMap.values();
        Map<String, List<Menu>> resourcesParentMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(menus)) {
            for (Menu menu : menus) {
                //只有用户拥有的菜单才能分配,或者用户是最高级用户
                if (AuthorityUtil.hasRole(BaseRole.ROLE_ROOT) || menusIds.contains(menu.getId())) {
                    List<Menu> resourcesChild = resourcesParentMap.get(menu.getParentId());
                    if (resourcesChild == null) {
                        resourcesChild = new ArrayList<>();
                        resourcesParentMap.put(menu.getParentId(), resourcesChild);
                    }
                    resourcesChild.add(menu);
                }
            }
        }
        ZTreeNode rootNode = new ZTreeNode();
        rootNode.setId("0");
        rootNode.setName("根");
        return getTreeNode(rootNode, resourcesParentMap, hasResourceIds);
    }

    public Set<String> getMenuIds(String roleId) {
        Set<String> menuIds = new HashSet<>();
        List<RoleMenu> roleMenus = roleMenuDao.findByRoleId(roleId);
        if (CollectionUtil.isNotEmpty(roleMenus)) {
            for (RoleMenu roleMenu : roleMenus) {
                menuIds.add(roleMenu.getMenuId());
            }
        }
        return menuIds;
    }

    private static ZTreeNode getTreeNode(ZTreeNode node, Map<String, List<Menu>> resourcesParentMap, Set<String> hasResourceIds) {
        List<Menu> childMenus = resourcesParentMap.get(node.getId());
        if (CollectionUtil.isNotEmpty(childMenus)) {
            new ListSortUtil<Menu>().sortInteger(childMenus, "seq", "desc");
            for (Menu menu : childMenus) {
                ZTreeNode treeNode = new ZTreeNode();
                if (hasResourceIds.contains(menu.getId())) {
                    treeNode.setChecked(true);
                } else {
                    treeNode.setChecked(false);
                }
                treeNode.setName(menu.getName());
                treeNode.setId(menu.getId());
                treeNode.setpId(menu.getParentId());
//                treeNode.setUrl(menu.getUrl());

                node.getChildren().add(getTreeNode(treeNode, resourcesParentMap, hasResourceIds));
            }
        }
        return node;
    }

    public Set<String> getResourceIds(String roleId) {
        Set<String> resourceIds = new HashSet<>();
        List<RoleMenu> roleMenus = roleMenuDao.findByRoleId(roleId);
        if (CollectionUtil.isNotEmpty(roleMenus)) {
            for (RoleMenu roleMenu : roleMenus) {
                resourceIds.add(roleMenu.getMenuId());
            }
        }
        return resourceIds;
    }

    //获取角色id与菜单的对应关系
    public static Map<String, Set<Menu>> getRoleMenuMap() {
        Map<String, Set<Menu>> roleMenuMap = new HashMap<>();
        Collection<RoleMenu> roleMenus = RoleMenuService.roleMenuMap.values();
        if (CollectionUtil.isNotEmpty(roleMenus)) {
            for (RoleMenu roleMenu : roleMenus) {
                Set<Menu> menuSet = roleMenuMap.get(roleMenu.getRoleId());
                if (menuSet == null) {
                    menuSet = new HashSet<>();
                    roleMenuMap.put(roleMenu.getRoleId(), menuSet);
                }
                Menu menu = MenuService.menuMap.get(roleMenu.getMenuId());
                if (menu != null) {
                    menuSet.add(menu);
                }
            }
        }
        return roleMenuMap;
    }

    public Set<Menu> getRoleMenus(String roleId) {
        Set<Menu> menuSet = new HashSet<>();
        Collection<RoleMenu> roleMenus = RoleMenuService.roleMenuMap.values();
        for (RoleMenu roleMenu : roleMenus) {
            if (roleId.equals(roleMenu.getRoleId())) {
                Menu menu = MenuService.menuMap.get(roleMenu.getMenuId());
                if (menu != null) {
                    menuSet.add(menu);
                }
            }
        }
        return menuSet;
    }

    //获取菜单与角色的对应关系
    public static Map<String, Set<Role>> getMenuRoleMap() {
        Collection<RoleMenu> roleMenus = RoleMenuService.roleMenuMap.values();
        Map<String, Set<Role>> menuRoleMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(roleMenus)) {
            for (RoleMenu roleMenu : roleMenus) {
                Set<Role> rolesSet = menuRoleMap.get(roleMenu.getMenuId());
                if (rolesSet == null) {
                    rolesSet = new HashSet<>();
                    menuRoleMap.put(roleMenu.getMenuId(), rolesSet);
                }
                Role role = RoleService.roleIdRoleMap.get(roleMenu.getRoleId());
                if (role != null) {
                    rolesSet.add(role);
                    menuRoleMap.put(roleMenu.getMenuId(), rolesSet);
                }
            }
        }
        return menuRoleMap;
    }

    public void updateRoleMenu(String roleId, String[] addIds) {
        List<RoleMenu> roleMenus = roleMenuDao.findByRoleId(roleId);
        List<String> roleMenuIds = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(roleMenus)) {
            for (RoleMenu roleMenu : roleMenus) {
                roleMenuIds.add(roleMenu.getId());
            }
        }
        String hql = "delete from RoleMenu t where t.roleId=:roleId";
        Query query = roleMenuDao.createHQLQuery(hql);
        query.setParameter("roleId", roleId);
        query.executeUpdate();
        if (ArrayUtils.isNotEmpty(addIds)) {
            for (String id : addIds) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(id);
                roleMenuDao.save(roleMenu);
            }
        }
        roleMenuDao.flush();
        initRoleMenuCache(roleMenuIds, roleId);
        MySecurityMetadataSource.resourceMap = RoleResourceService.getResourceAuthority();
    }

    public void initRoleMenuCache() {
        roleMenuMap.clear();
        List<RoleMenu> os = roleMenuDao.findAll();
        if (CollectionUtil.isNotEmpty(os)) {
            for (RoleMenu o : os) {
                roleMenuMap.put(o.getId(), o);
            }
        }
    }

    public void initRoleMenuCache(List<String> oriRoleMenuIds, String roleId) {
        if (CollectionUtil.isNotEmpty(oriRoleMenuIds)) {
            for (String oriRoleMenuId : oriRoleMenuIds) {
                roleMenuMap.remove(oriRoleMenuId);
            }
        }
        List<RoleMenu> os = roleMenuDao.findByRoleId(roleId);
        if (CollectionUtil.isNotEmpty(os)) {
            for (RoleMenu o : os) {
                roleMenuMap.put(o.getId(), o);
            }
        }
    }
}
