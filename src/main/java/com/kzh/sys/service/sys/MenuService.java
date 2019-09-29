package com.kzh.sys.service.sys;

import com.kzh.sys.app.utils.SessionUtil;
import com.kzh.sys.core.dao.GenericRepository;
import com.kzh.sys.core.dao.Restrictions;
import com.kzh.sys.core.dao.SimpleExpression;
import com.kzh.sys.dao.MenuDao;
import com.kzh.sys.model.Menu;
import com.kzh.sys.model.Role;
import com.kzh.sys.pojo.Result;
import com.kzh.sys.pojo.tree.BaseTreeNode;
import com.kzh.sys.pojo.tree.ZTreeNode;
import com.kzh.sys.util.CollectionUtil;
import com.kzh.sys.util.ListSortUtil;
import com.kzh.sys.util.SysUtil;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
public class MenuService extends BaseService<Menu> {
    private static final Logger logger = Logger.getLogger(MenuService.class);
    //id与resource实体的缓存
    public static Map<String, Menu> menuMap = new ConcurrentHashMap<>();
    //url与menu实体的缓存
    public static Map<String, Menu> urlMenuMap = new ConcurrentHashMap<>();

    @Resource
    private MenuDao menuDao;
    @Resource
    private RoleMenuService roleMenuService;

    @Override
    public GenericRepository getDao() {
        return menuDao;
    }

    public Menu saveMenu(Menu menu) throws Exception {
        if (SysUtil.isEmpty(menu.getId())) {
            Menu menuSaved = menuDao.save(menu);
            MenuService.menuMap.put(menuSaved.getId(), menuSaved);
            MenuService.urlMenuMap.put(menuSaved.getUrl(), menuSaved);
            return menuSaved;
        } else {
            Menu menuDb = menuDao.getOne(menu.getId());
            menuDb.initModifyFields(menu);
            MenuService.menuMap.put(menu.getId(), menuDb);
            MenuService.urlMenuMap.put(menu.getUrl(), menuDb);
            menuDao.save(menuDb);
            return menuDb;
        }
    }

    public Page<Menu> getMenuPage(Pageable pageable, String menuName) {
        List<SimpleExpression> expressions = new ArrayList<>();
        if (SysUtil.isNotEmpty(menuName)) {
            expressions.add(Restrictions.like("name", menuName, true));
        }
        return menuDao.findByPage(expressions, pageable);
    }

    public List<Menu> getAll() {
        return menuDao.findAll();
    }

    //根据父节点的id查询该节点下的所有菜单
    public List queryMenuList(String parentId, String username) {
        String sql = "select " +
                "    t3.id," +
                "    t3.resource_name," +
                "    t3.resource_key," +
                "    (select " +
                "            count(*)" +
                "        from" +
                "            menu t6" +
                "        where" +
                "            t6.parent_id = t3.id) child_count" +
                " from" +
                "    user t1," +
                "    role t2," +
                "    menu t3," +
                "    user_role t4," +
                "    resource_role t5" +
                " where" +
                "    t1.id = t4.user_id" +
                "        and t2.id = t4.role_id" +
                "        and t3.id = t5.resource_id" +
                "        and t2.id = t5.role_id" +
                "        and t3.parent_id='" + parentId +
                "'       and t1.username='" + username +
                "' order by t3.seq";
        List list = menuDao.findBySql(sql, null);
        List menuList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            Object[] objs = (Object[]) list.get(i);
            map.put("id", objs[0]);
            map.put("text", objs[1]);
            if (Integer.valueOf(objs[3].toString()) != 0) {
                map.put("state", "closed");
            }
            Map<String, Object> mapUrl = new HashMap<>();
            mapUrl.put("url", objs[2]);
            map.put("attributes", mapUrl);
            menuList.add(map);
        }
        return menuList;
    }

    public Result delMenu(String[] ids) {
        List<SimpleExpression> expressionsCheck = new ArrayList<>();
        expressionsCheck.add(Restrictions.in("parentId", CollectionUtil.arrayToSet(ids), true));
        List<Menu> menuParents = menuDao.findAll(expressionsCheck);
        if (CollectionUtil.isNotEmpty(menuParents)) {
            return new Result(false, "删除失败！含有子菜单");
        }

        List<SimpleExpression> expressions = new ArrayList<>();
        expressions.add(Restrictions.in("id", CollectionUtil.arrayToSet(ids), true));
        List<Menu> menus = menuDao.findAll(expressions);
        if (CollectionUtil.isNotEmpty(menus)) {
            for (Menu menu : menus) {
                MenuService.menuMap.remove(menu.getId());
                MenuService.urlMenuMap.remove(menu.getUrl());
            }
        }
        menuDao.deleteInBatch(menus);
        return new Result(true, "成功");
    }

    public ZTreeNode getRoleMenuTree(String roleId) {
        Set<String> hasResourceIds = roleMenuService.getResourceIds(roleId);
        Collection<Menu> menus = MenuService.menuMap.values();
        Map<String, List<Menu>> resourcesParentMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(menus)) {
            for (Menu menu : menus) {
                List<Menu> resourcesChild = resourcesParentMap.get(menu.getParentId());
                if (resourcesChild == null) {
                    resourcesChild = new ArrayList<>();
                    resourcesParentMap.put(menu.getParentId(), resourcesChild);
                }
                resourcesChild.add(menu);
            }
        }
        ZTreeNode rootNode = new ZTreeNode();
        rootNode.setId("0");
        rootNode.setName("根");
        return getTreeNode(rootNode, resourcesParentMap, hasResourceIds);
    }

    public ZTreeNode getMenuTree() {
        Set<String> hasResourceIds = new HashSet<>();
        Collection<Menu> menus = MenuService.menuMap.values();
        Map<String, List<Menu>> menusParentMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(menus)) {
            for (Menu menu : menus) {
                List<Menu> menusChild = menusParentMap.get(menu.getParentId());
                if (menusChild == null) {
                    menusChild = new ArrayList<>();
                    menusParentMap.put(menu.getParentId(), menusChild);
                }
                menusChild.add(menu);
            }
        }
        ZTreeNode rootNode = new ZTreeNode();
        rootNode.setId("0");
        rootNode.setName("根");
        rootNode.setOpen(true);
        return getTreeNode(rootNode, menusParentMap, hasResourceIds);
    }

    public BaseTreeNode getAceResourceTree(HttpSession session, String activeUrl) {
        BaseTreeNode rootNode = new BaseTreeNode();
        rootNode.setId("0");
        rootNode.setName("根");

        Role role = SessionUtil.getRole();
        Set<Menu> menuSet = roleMenuService.getRoleMenus(role.getId());
        if (CollectionUtil.isEmpty(menuSet)) {
            logger.info("未指定角色或者角色未关联资源");
            return rootNode;
        }
        Map<String, List<Menu>> resourcesParentMap = new HashMap<>();
        for (Menu menu : menuSet) {
            List<Menu> resourcesChild = resourcesParentMap.get(menu.getParentId());
            if (resourcesChild == null) {
                resourcesChild = new LinkedList<>();
                resourcesParentMap.put(menu.getParentId(), resourcesChild);
            }
            resourcesChild.add(menu);
        }
        BaseTreeNode baseTreeNode = getTreeNode(rootNode, resourcesParentMap, activeUrl);
        return baseTreeNode;
    }

    public static List<BaseTreeNode> getNodeNav(String activeUrl) {
        List<BaseTreeNode> baseTreeNodeList = new ArrayList<>();
        Menu menu = urlMenuMap.get(activeUrl);
        if (menu == null) {
            return baseTreeNodeList;
        }
        do {
            BaseTreeNode baseTreeNode = new BaseTreeNode();
            baseTreeNode.setId(menu.getId());
            baseTreeNode.setName(menu.getName());
            baseTreeNode.setUrl(menu.getUrl());
            baseTreeNode.setIcon(menu.getIcon());
            baseTreeNodeList.add(baseTreeNode);
            menu = menuMap.get(menu.getParentId());
        } while (menu != null);
        List<BaseTreeNode> sortedTreeNodes = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(baseTreeNodeList)) {
            for (int i = baseTreeNodeList.size() - 1; i >= 0; i--) {
                sortedTreeNodes.add(baseTreeNodeList.get(i));
            }
        }

        return sortedTreeNodes;
    }

    public static List<String> getMenuNav(String activeUrl) {
        List<String> navs = new ArrayList<>();
        Menu menu = urlMenuMap.get(activeUrl);
        if (menu == null) {
            return navs;
        }
        do {
            navs.add(menu.getName());
            menu = menuMap.get(menu.getParentId());
        } while (menu != null);
        List<String> sortedTreeNodes = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(navs)) {
            for (int i = navs.size() - 1; i >= 0; i--) {
                sortedTreeNodes.add(navs.get(i));
            }
        }

        return sortedTreeNodes;
    }

    private static BaseTreeNode getTreeNode(BaseTreeNode node, Map<String, List<Menu>> resourcesParentMap, String activeUrl) {
        List<Menu> childMenus = resourcesParentMap.get(node.getId());
        if (CollectionUtil.isNotEmpty(childMenus)) {
            new ListSortUtil<Menu>().sortInteger(childMenus, "seq", "desc");
            for (Menu menu : childMenus) {
                BaseTreeNode treeNode = new BaseTreeNode();
                treeNode.setId(menu.getId());
                treeNode.setName(menu.getName());
                treeNode.setUrl(menu.getUrl());
                treeNode.setIcon(menu.getIcon());
                if (SysUtil.isNotEmpty(activeUrl) && activeUrl.equals(menu.getUrl())) {
                    treeNode.setChecked(true);
                    node.setChecked(true);
                }

                node.getChildren().add(getTreeNode(treeNode, resourcesParentMap, activeUrl));
            }
        }
        return node;
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
                treeNode.setName(menu.getName() + " " + menu.getSeq());
                treeNode.setId(menu.getId());
                treeNode.setpId(menu.getParentId());

                node.getChildren().add(getTreeNode(treeNode, resourcesParentMap, hasResourceIds));
            }
        }
        return node;
    }

    public void move(String sourceId, String targetId) {
        Menu menu = menuDao.getOne(sourceId);
        if (menu != null) {
            menu.setParentId(targetId);
            menuDao.save(menu);
        }

        Menu menu1 = MenuService.menuMap.get(sourceId);
        if (menu1 != null) {
            menu1.setParentId(targetId);
        }
    }

    public void initMenu() {
        List<Menu> os = menuDao.findAll();
        if (CollectionUtil.isNotEmpty(os)) {
            for (Menu o : os) {
                MenuService.menuMap.put(o.getId(), o);


                MenuService.urlMenuMap.put(o.getUrl(), o);
            }
        }

    }

    public static String getFirstMenu(BaseTreeNode treeNode) {
        if (CollectionUtil.isNotEmpty(treeNode.getChildren())) {
            for (BaseTreeNode treeNode1 : treeNode.getChildren()) {
                if (SysUtil.isNotEmpty(treeNode1.getUrl())) {
                    return treeNode1.getUrl();
                } else {
                    return getFirstMenu(treeNode1);
                }
            }
        }
        return null;
    }
}