package com.kzh.sys.controller;

import com.kzh.sys.model.Msg;
import com.kzh.sys.pojo.Result;
import com.kzh.sys.service.generate.FieldService;
import com.kzh.sys.service.sys.MsgService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/sys/msg")
public class MsgController {
    @Resource
    private MsgService service;

    @RequestMapping(value = "/save")
    @ResponseBody
    public Object save(Msg msg) throws Exception {
        Result result = service.saveMsg(msg);
        return result;
    }

    @RequestMapping(value = "/del")
    @ResponseBody
    public Object del(String[] ids) {
        service.del(ids);
        return new Result(true);
    }

    @RequestMapping(value = "/mark")
    @ResponseBody
    public Object mark(String[] ids) {
        service.mark(ids);
        return new Result(true);
    }

    @RequestMapping(value = "/edit")
    public String edit(Model model, String id) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(Msg.class));
        Msg object = service.get(id);
        model.addAttribute("obj", object);
        return "/sys/msg/edit";
    }

    @RequestMapping(value = "/add")
    public String add(Model model) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(Msg.class));
        return "/sys/msg/add";
    }

    @RequestMapping(value = "/view")
    public String view(Model model, String id) {
        model.addAttribute("fieldMap", FieldService.getFieldMap(Msg.class));
        Msg object = service.get(id);
        model.addAttribute("obj", object);
        return "/sys/msg/view";
    }
}
