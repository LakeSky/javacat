package com.kzh.sys.controller;

import com.kzh.sys.model.Log;
import com.kzh.sys.pojo.Result;
import com.kzh.sys.pojo.WorldPage;
import com.kzh.sys.service.generate.FieldService;
import com.kzh.sys.service.sys.LogService;
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
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping(value = "/sys/log", name = "系统日志管理")
public class LogController {
    private static final Logger logger = Logger.getLogger(Log.class);

    @Resource
    private LogService service;
    @Resource
    private FieldService fieldService;

    @RequestMapping(value = "/home")
    public String home(Model model) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(Log.class));
        Map<String, Set<String>> map = service.categories();
        model.addAttribute("categories", map.get("categories"));
        model.addAttribute("actions", map.get("actions"));
        return "/sys/log/home";
    }

    @RequestMapping(value = "/page", name = "分页查询")
    @ResponseBody
    public Object page(HttpServletRequest request, Log entity, WorldPage worldPage) throws Exception {
        worldPage.setProperties("createTime");
        Page<Log> pages = service.page(entity, worldPage);
        return pages;
    }

    @RequestMapping(value = "/objLog")
    @ResponseBody
    public Object objLog(HttpServletRequest request, String objId) throws Exception {
        List<Log> logs = service.objLog(objId);
        return new Result(true, "", logs);
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    public Object save(Log entity) throws Exception {
        Result result = service.saveLog(entity);
        return result;
    }

    @RequestMapping(value = "/del", name = "删除")
    @ResponseBody
    public Object del(String[] ids) {
        service.del(ids);
        return new Result(true);
    }

    @RequestMapping(value = "/add", name = "添加")
    public String add(Model model) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(Log.class));
        model.addAttribute("page", "add");
        return "/sys/log/edit";
    }

    @RequestMapping(value = "/edit", name = "编辑")
    public String edit(Model model, String id) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(Log.class));
        Log object = service.get(id);
        model.addAttribute("obj", object);
        model.addAttribute("page", "edit");
        return "/sys/log/edit";
    }

    @RequestMapping(value = "/view", name = "查看")
    public String view(Model model, String id) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(Log.class));
        Log object = service.get(id);
        model.addAttribute("obj", object);
        model.addAttribute("page", "view");
        return "/sys/log/view";
    }

    @RequestMapping(value = "/viewDetail", name = "查看详细信息")
    public String viewDetail(Model model, String id) {
        Log object = service.get(id);
        model.addAttribute("obj", object);
        model.addAttribute("page", "viewFailMsg");
        return "/sys/log/viewDetail";
    }

    @RequestMapping(value = "/export", name = "导出")
    public void exportExcel(HttpServletResponse response, String params, String fileName) throws Exception {
        OutputStream os = null;
        String dateStr = DateUtil.format("yyyyMMddHHmmss", new Date());
        String filename = fileName + dateStr + ".xls";//设置下载时客户端Excel的名称
        filename = new String(filename.getBytes("utf-8"), "iso-8859-1");//处理中文文件名
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        os = response.getOutputStream();
        Workbook workbook = fieldService.export(params, Log.class);
        workbook.write(os);
        os.flush();
        os.close();
    }
}
