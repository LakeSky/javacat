package com.kzh.sys.controller;

import com.kzh.sys.app.utils.SessionUtil;
import com.kzh.sys.model.Role;
import com.kzh.sys.model.User;
import com.kzh.sys.pojo.Result;
import com.kzh.sys.pojo.WorldPage;
import com.kzh.sys.service.generate.FieldService;
import com.kzh.sys.service.sys.RoleService;
import com.kzh.sys.service.sys.UserService;
import com.kzh.sys.util.DateUtil;
import com.kzh.sys.util.SysUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/sys/user", name = "用户管理")
public class UserController {
    @Resource
    private UserService service;
    @Resource
    private RoleService roleService;
    @Resource
    private FieldService fieldService;

    @RequestMapping(value = "/home")
    public String home(Model model) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(User.class));
        List<Role> roles = roleService.getCanSelect();
        model.addAttribute("roles", roles);
        return "sys/user/home";
    }

    @RequestMapping(value = "/edit", name = "编辑")
    public String edit(Model model, String id) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(User.class));
        User object = service.get(id);
        model.addAttribute("obj", object);
        model.addAttribute("page", "edit");
        model.addAttribute("roles", roleService.getCanSelect());
        return "/sys/user/edit";
    }

    @RequestMapping(value = "/add", name = "添加")
    public String add(Model model) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(User.class));
        model.addAttribute("page", "add");
        model.addAttribute("roles", roleService.getCanSelect());
        return "/sys/user/edit";
    }

    @RequestMapping(value = "/view", name = "-查看")
    public String view(Model model, String id) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(User.class));
        User object = service.get(id);
        model.addAttribute("obj", object);
        model.addAttribute("page", "view");
        return "/sys/user/edit";
    }

    @RequestMapping(value = "/page", name = "分页查询")
    @ResponseBody
    public Object page(HttpServletRequest request, User entity, WorldPage worldPage) throws Exception {
        worldPage.setProperties("createTime");
        Page<User> pages = service.findPage(entity, worldPage);
        return pages;
    }

    @RequestMapping(value = "/set")
    public String set(Model model) {
        return "sys/user/set";
    }

    @RequestMapping(value = "/center")
    public String center(Model model) {
        User user = service.getByUsername(SessionUtil.getUserName());
        model.addAttribute("user", user);
        return "sys/user/center";
    }

    @RequestMapping(value = "/role/save", name = "选择角色")
    @ResponseBody
    public Object saveUserRole(String userId, String roleId) {
        service.saveRole(userId, roleId);
        return new Result(true, "保存成功");
    }

    @RequestMapping(value = "/roles")
    @ResponseBody
    public Object roles(String id) {
        Result result = service.getUserRoles(id);
        return result;
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    public Object save(User entity) throws Exception {
        Result result = service.saveUser(entity);
        return result;
    }

    @RequestMapping(value = "/del", name = "删除")
    @ResponseBody
    public Object del(String[] ids) {
        service.del(ids);
        return new Result(true);
    }

    @RequestMapping(value = "/modifyPassword", name = "-修改自己密码")
    @ResponseBody
    public Object modifyPassword(String oldPassword, String newPassword, String rePassword) {
        Result result = service.modifyPassword(SessionUtil.getUserName(), oldPassword, newPassword, rePassword);
        return result;
    }

    @RequestMapping(value = "/password/modify", name = "-修改某用户的密码")
    @ResponseBody
    public Object passwordModify(String id, String oldPassword, String newPassword, String rePassword) {
        User user = service.get(id);
        Result result = service.modifyPassword(user.getUsername(), oldPassword, newPassword, rePassword);
        return result;
    }

    @RequestMapping(value = "/export", name = "导出")
    public void exportExcel(HttpServletResponse response, String params, String o, String fileName) throws Exception {
        if (SysUtil.isEmpty(o)) {
            return;
        }
        OutputStream os = null;
        String dateStr = DateUtil.format("yyyyMMddHHmmss", new Date());
        String filename = fileName + dateStr + ".xls";//设置下载时客户端Excel的名称
        filename = new String(filename.getBytes("utf-8"), "iso-8859-1");//处理中文文件名
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        os = response.getOutputStream();
        Workbook workbook = fieldService.export(params, User.class);
        workbook.write(os);
        os.flush();
        os.close();
    }
}
