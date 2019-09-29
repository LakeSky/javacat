package com.kzh.busi.controller;

import com.kzh.busi.model.Employee;
import com.kzh.busi.service.EmployeeService;
import com.kzh.sys.pojo.Result;
import com.kzh.sys.pojo.WorldPage;
import com.kzh.sys.service.generate.FieldService;
import com.kzh.sys.util.DateUtil;
import org.apache.log4j.Logger;
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

@Controller
@RequestMapping(value = "/busi/employee", name = "员工管理")
public class EmployeeController {
    private static final Logger logger = Logger.getLogger(Employee.class);
    
    @Resource
    private EmployeeService service;
    @Resource
    private FieldService fieldService;

    @RequestMapping(value = "/home")
    public String home(Model model) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(Employee.class));
        return "/busi/employee/home";
    }

    @RequestMapping(value = "/page", name = "-分页查询")
    @ResponseBody
    public Object page(HttpServletRequest request, Employee entity, WorldPage worldPage) throws Exception{
        worldPage.setProperties("createTime");
        Page<Employee> pages = service.page(entity, worldPage);
        return pages;
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    public Object save(Employee entity) throws Exception{
        Result result=service.saveEmployee(entity);
        return result;
    }

    @RequestMapping(value = "/del", name = "-删除")
    @ResponseBody
    public Object del(String[] ids) {
        service.del(ids);
        return new Result(true);
    }

    @RequestMapping(value = "/add", name = "-添加")
    public String add(Model model) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(Employee.class));
        model.addAttribute("page", "add");
        return "/busi/employee/edit";
    }

    @RequestMapping(value = "/edit", name = "-编辑")
    public String edit(Model model, String id) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(Employee.class));
        Employee object = service.get(id);
        model.addAttribute("obj", object);
        model.addAttribute("page", "edit");
        return "/busi/employee/edit";
    }

    @RequestMapping(value = "/view", name = "-查看")
    public String view(Model model, String id) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(Employee.class));
        Employee object = service.get(id);
        model.addAttribute("obj", object);
        model.addAttribute("page", "view");
        return "/busi/employee/view";
    }

    @RequestMapping(value = "/export", name = "-导出")
    public void exportExcel(HttpServletResponse response, String params, String fileName) throws Exception {
        OutputStream os = null;
        String dateStr = DateUtil.format("yyyyMMddHHmmss", new Date());
        String filename = fileName + dateStr + ".xls";//设置下载时客户端Excel的名称
        filename = new String(filename.getBytes("utf-8"), "iso-8859-1");//处理中文文件名
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        os = response.getOutputStream();
        Workbook workbook = fieldService.export(params, Employee.class);
        workbook.write(os);
        os.flush();
        os.close();
    }
}
