package com.kzh.sys.controller;

import com.kzh.sys.app.utils.SessionUtil;
import com.kzh.sys.model.Role;
import com.kzh.sys.pojo.Result;
import com.kzh.sys.pojo.WorldPage;
import com.kzh.sys.pojo.tree.ZTreeNode;
import com.kzh.sys.service.generate.FieldService;
import com.kzh.sys.service.sys.RoleMenuService;
import com.kzh.sys.service.sys.RoleResourceService;
import com.kzh.sys.service.sys.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping(value = "/sys/role", name = "角色管理")
public class RoleController {
    @Resource
    private RoleService service;
    @Resource
    private RoleMenuService roleMenuService;
    @Resource
    private RoleResourceService roleResourceService;

    @RequestMapping(value = "/home")
    public String home(Model model) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(Role.class));
        return "/sys/role/home";
    }

    @RequestMapping(value = "/edit", name = "编辑")
    public String edit(Model model, String id) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(Role.class));
        model.addAttribute("page", "edit");
        Role object = service.get(id);
        model.addAttribute("obj", object);
        model.addAttribute("dataLevels", service.getCanSelDataLevels(SessionUtil.getUserName()));
        return "/sys/role/edit";
    }

    @RequestMapping(value = "/add", name = "添加")
    public String add(Model model) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(Role.class));
        model.addAttribute("page", "add");
        model.addAttribute("dataLevels", service.getCanSelDataLevels(SessionUtil.getUserName()));
        return "/sys/role/edit";
    }

    @RequestMapping(value = "/menu", name = "配置菜单")
    public String menu(Model model, String id) {
        model.addAttribute("role", service.getRole(id));
        return "sys/role/menu";
    }

    @RequestMapping(value = "/resource", name = "配置权限")
    public String resource(Model model, String id) {
        model.addAttribute("role", service.getRole(id));
        return "sys/role/resource";
    }

    @RequestMapping(value = "/page", name = "分页查询")
    @ResponseBody
    public Object page(HttpServletRequest request, Role entity, WorldPage worldPage) throws Exception {
        worldPage.setProperties("createTime");
        Page<Role> pages = service.page(entity, worldPage);
        return pages;
    }

    @RequestMapping(value = "/del", name = "删除")
    @ResponseBody
    public Object del(String[] ids) {
        service.del(ids);
        return new Result(true);
    }

    @RequestMapping(value = "/save", name = "保存")
    @ResponseBody
    public Object save(Role role) {
        Result result = service.saveRole(role);
        return result;
    }

    @RequestMapping(value = "/menuTree")
    @ResponseBody
    public Object menuTree(String roleId) {
        ZTreeNode zTreeNode = roleMenuService.getRoleMenuTree(SessionUtil.getUserName(), roleId);
        return zTreeNode.getChildren();
    }

    @RequestMapping(value = "/saveRoleMenu")
    @ResponseBody
    public Object updateRoleMenu(String roleId, String[] addIds) {
        roleMenuService.updateRoleMenu(roleId, addIds);
        return new Result(true, "成功");
    }

    @RequestMapping(value = "/saveRoleResource")
    @ResponseBody
    public Object saveRoleResource(String roleId, String[] addIds) {
        roleResourceService.saveRoleResource(roleId, addIds);
        return new Result(true, "成功");
    }
}
