package com.kzh.sys.service.sys;

import com.kzh.busi.enums.BaseRole;
import com.kzh.sys.app.utils.AppUtils;
import com.kzh.sys.app.utils.AuthorityUtil;
import com.kzh.sys.core.dao.GenericRepository;
import com.kzh.sys.dao.MsgDao;
import com.kzh.sys.dao.RoleDao;
import com.kzh.sys.dao.RoleResourceDao;
import com.kzh.sys.model.*;
import com.kzh.sys.pojo.tree.ZTreeNode;
import com.kzh.sys.service.sys.security.MySecurityMetadataSource;
import com.kzh.sys.util.CollectionUtil;
import com.kzh.sys.util.NumberUtil;
import com.kzh.sys.util.SysUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.persistence.Query;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
public class RoleResourceService extends BaseService<RoleResource> {
    private static final Logger logger = Logger.getLogger(RoleResourceService.class);
    public static Map<String, RoleResource> roleResourceMap = new ConcurrentHashMap<>();
    public static Map<String, String> urlNameMap = new ConcurrentHashMap<>();

    @Resource
    private RoleResourceDao roleResourceDao;
    @Resource
    private RoleDao roleDao;
    @Resource
    private MsgDao msgDao;

    @Override
    public GenericRepository getDao() {
        return roleResourceDao;
    }

    public void saveRoleResource(String roleId, String[] selUrls) {
        calcUrlChange(roleId, selUrls);
        String hql = "delete from RoleResource t where t.roleId=:roleId";
        Query query = roleResourceDao.createHQLQuery(hql);
        query.setParameter("roleId", roleId);
        query.executeUpdate();
        if (ArrayUtils.isNotEmpty(selUrls)) {
            for (String url : selUrls) {
                RoleResource roleResource = new RoleResource();
                roleResource.setRoleId(roleId);
                roleResource.setUrl(url);
                roleResourceDao.save(roleResource);
            }
        }
        roleResourceDao.flush();
        initCache();
        MySecurityMetadataSource.resourceMap = getResourceAuthority();
    }

    public void calcUrlChange(String roleId, String[] addIds) {
        //需要记录增加了那些权限，减少了那些权限
        Role role = roleDao.getOne(roleId);
        //原来有的权限
        List<RoleResource> roleResourceOris = roleResourceDao.findByRoleId(roleId);
        Set<String> roleOriIds = new HashSet<>();
        if (CollectionUtil.isNotEmpty(roleResourceOris)) {
            for (RoleResource roleResource : roleResourceOris) {
                roleOriIds.add(roleResource.getUrl());
            }
        }
        List<String> urlAdds = new ArrayList<>();
        Set<String> roleNowIds = new HashSet<>();
        if (ArrayUtils.isNotEmpty(addIds)) {
            roleNowIds.addAll(Arrays.asList(addIds));
        }

        //减少的权限
        Collection<String> urlsSub = CollectionUtils.subtract(roleOriIds, roleNowIds);
        //增加的权限
        Collection<String> urlsAdd = CollectionUtils.subtract(roleNowIds, roleOriIds);

        String addUrls = "";
        String subUrls = "";
        if (CollectionUtil.isNotEmpty(urlsAdd)) {
            addUrls += "增加权限：";
            for (String url : urlsAdd) {
                String name = RoleResourceService.urlNameMap.get(url);
                if (SysUtil.isNotEmpty(name)) {
                    addUrls += name + ",";
                }
            }
        }
        if (CollectionUtil.isNotEmpty(urlsSub)) {
            subUrls += "减少权限：";
            for (String url : urlsSub) {
                String name = RoleResourceService.urlNameMap.get(url);
                if (SysUtil.isNotEmpty(name)) {
                    subUrls += name + ",";
                }
            }
        }
        String content = "";
        if (SysUtil.isNotEmpty(addUrls) || SysUtil.isNotEmpty(subUrls)) {
            content = addUrls + subUrls;
        }
        Msg msg = new Msg(roleId, "", "选择权限", "您的账号" + content, role.getRoleName());
        msgDao.save(msg);
        //----------------------------------
    }

    public void initCache() {
        roleResourceMap.clear();
        List<RoleResource> os = roleResourceDao.findAll();
        if (CollectionUtil.isNotEmpty(os)) {
            for (RoleResource o : os) {
                roleResourceMap.put(o.getId(), o);
            }
        }
        MySecurityMetadataSource.resourceMap = getResourceAuthority();
    }

    public static Map<String, Collection<ConfigAttribute>> getResourceAuthority() {
        Map<String, Collection<ConfigAttribute>> resourceMap = new HashMap<>();
        Collection<RoleResource> roleResources = RoleResourceService.roleResourceMap.values();
        Map<String, Set<Role>> urlRoleSetMap = getResourceRoleMap();
        List<String> urls = new ArrayList<>();
        //权限控制的url
        for (RoleResource roleResource : roleResources) {
            urls.add(roleResource.getUrl());
        }
        //菜单控制的url
        for (RoleMenu roleMenu : RoleMenuService.roleMenuMap.values()) {
            Menu menu = MenuService.menuMap.get(roleMenu.getMenuId());
            if (menu != null) {
                urls.add(menu.getUrl());
            }
        }
        for (String url : urls) {
            Set<Role> roles = urlRoleSetMap.get(url);
            if (CollectionUtil.isNotEmpty(roles)) {
                Collection<ConfigAttribute> configAttributes = new ArrayList<>();
                for (Role role : roles) {
                    ConfigAttribute configAttribute = new SecurityConfig(role.getRoleKey());
                    configAttributes.add(configAttribute);
                }
                resourceMap.put(url, configAttributes);
            }
        }
        return resourceMap;
    }

    public static Map<String, Set<Role>> getResourceRoleMap() {
        Collection<RoleResource> roleResources = roleResourceMap.values();
        Map<String, Set<Role>> resourceRoleMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(roleResources)) {
            for (RoleResource roleResource : roleResources) {
                Set<Role> rolesSet = resourceRoleMap.get(roleResource.getUrl());
                if (rolesSet == null) {
                    rolesSet = new HashSet<>();
                    resourceRoleMap.put(roleResource.getUrl(), rolesSet);
                }
                Role role = RoleService.roleIdRoleMap.get(roleResource.getRoleId());
                if (role != null) {
                    rolesSet.add(role);
                }
            }
        }
        //将Menu也作为资源加给对应的角色
        Collection<RoleMenu> roleMenus = RoleMenuService.roleMenuMap.values();
        if (CollectionUtil.isNotEmpty(roleMenus)) {
            for (RoleMenu roleMenu : roleMenus) {
                Menu menu = MenuService.menuMap.get(roleMenu.getMenuId());
                if (menu != null) {
                    Set<Role> rolesSet = resourceRoleMap.get(menu.getUrl());
                    if (rolesSet == null) {
                        rolesSet = new HashSet<>();
                        resourceRoleMap.put(menu.getUrl(), rolesSet);
                    }
                    Role role = RoleService.roleIdRoleMap.get(roleMenu.getRoleId());
                    if (role != null) {
                        rolesSet.add(role);
                    }
                }
            }
        }
        return resourceRoleMap;
    }

    //查询某个权限的拥有者数目
    public Integer calcResourceOwns(String url) {
        String hql = "select count(*) from RoleResource t where t.url='" + url + "'";
        List data = roleResourceDao.findByHql(hql);
        return NumberUtil.intValue(data.get(0).toString());
    }
}
