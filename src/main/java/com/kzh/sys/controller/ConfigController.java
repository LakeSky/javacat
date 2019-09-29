package com.kzh.sys.controller;

import com.kzh.sys.model.Config;
import com.kzh.sys.pojo.Result;
import com.kzh.sys.service.generate.FieldService;
import com.kzh.sys.service.sys.ConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/sys/config", name = "配置管理")
public class ConfigController {
    @Resource
    private ConfigService service;

    @RequestMapping(value = "/common")
    public String common(Model model) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(Config.class));
        model.addAttribute("config", ConfigService.configMap);
        return "/sys/config/common";
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    public Object save(Config entity) throws Exception{
        Result result=service.saveConfig(entity);
        return result;
    }

    @RequestMapping(value = "/saveParam")
    @ResponseBody
    public Object saveParam(String configKey, String configValue) {
        Result result = service.saveParam(configKey, configValue);
        return result;
    }

    @RequestMapping(value = "/del")
    @ResponseBody
    public Object del(String[] ids) {
        service.del(ids);
        return new Result(true);
    }

    @RequestMapping(value = "/edit")
    public String edit(Model model, String id) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(Config.class));
        Config object = service.get(id);
        model.addAttribute("obj", object);
        return "/busi/config/edit";
    }

    @RequestMapping(value = "/add")
    public String add(Model model) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(Config.class));
        return "/busi/config/add";
    }

    @RequestMapping(value = "/view")
    public String view(Model model, String id) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(Config.class));
        Config object = service.get(id);
        model.addAttribute("obj", object);
        return "/busi/config/view";
    }
}
