package com.kzh.sys.controller;

import com.kzh.busi.enums.BaseRole;
import com.kzh.sys.app.utils.AuthorityUtil;
import com.kzh.sys.model.Menu;
import com.kzh.sys.pojo.Result;
import com.kzh.sys.pojo.tree.ZTreeNode;
import com.kzh.sys.service.sys.MenuService;
import com.kzh.sys.service.sys.ResourceService;
import com.kzh.sys.util.SysUtil;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/sys/menu", name = "菜单管理")
@Component
public class MenuController {
    @Resource
    private MenuService menuService;
    @Resource
    private ResourceService resourceService;

    @RequestMapping(value = "/home")
    public String home(Model model) {
        model.addAttribute("isAdmin", AuthorityUtil.hasRole(BaseRole.ROLE_ROOT));
        return "sys/menu/home";
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    public Object save(Menu menu) throws Exception {
        if (menu.getSeq() == null) {
            return new Result(false, "序号不能为空");
        }
        if (!SysUtil.isAllNotEmpty(menu.getName(), menu.getParentId())) {
            return new Result(false, "菜单名不能为空");
        }
        Menu menuDb = menuService.saveMenu(menu);
        ZTreeNode treeNode = new ZTreeNode();
        treeNode.setChecked(false);
        treeNode.setName(menuDb.getName() + " " + menuDb.getSeq());
        treeNode.setId(menuDb.getId());
        treeNode.setpId(menuDb.getParentId());
        return new Result(true, "", treeNode);
    }

    @RequestMapping(value = "/menuTree", name = "-菜单树")
    @ResponseBody
    public Object menuTree() {
        ZTreeNode treeNode = menuService.getMenuTree();
        List<ZTreeNode> nodes = new ArrayList<>();
        nodes.add(treeNode);
        return nodes;
    }

    @RequestMapping(value = "/move", name = "转移")
    @ResponseBody
    public Object move(String sourceId, String targetId) {
        menuService.move(sourceId, targetId);
        return new Result(true);
    }

    @RequestMapping(value = "/del", name = "删除")
    @ResponseBody
    public Object del(String[] ids) {
        Result result = menuService.delMenu(ids);
        return result;
    }

    @RequestMapping(value = "/info")
    @ResponseBody
    public Object info(String id) {
        Menu menu = menuService.get(id);
        return new Result(true, "成功", menu);
    }

    @RequestMapping(value = "/add", name = "添加")
    public void add() {
    }

    @RequestMapping(value = "/edit", name = "编辑")
    public void edit() {
    }

    //提取所有controller里面的页面
    @RequestMapping(value = "/resources")
    @ResponseBody
    public Object resources() {
        ZTreeNode zTreeNode = resourceService.resources();
        return zTreeNode.getChildren();
    }
}
