package com.kzh.sys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by gang on 2016/12/24.
 */
@Controller
public class HomeController {
    @RequestMapping(value = "/home")
    public String home() {
        return "redirect:back/home";
    }

    @RequestMapping(value = "/docs")
    public String docs() {
        return "docs";
    }

    @RequestMapping(value = "/error/nopermission")
    public String nopermission() {
        return "error/nopermission";
    }

    @RequestMapping(value = "/error/errorPage")
    public String error() {
        return "error/errorPage";
    }

    @RequestMapping(value = "/error/500")
    public String error500() {
        return "error/500";
    }
}
