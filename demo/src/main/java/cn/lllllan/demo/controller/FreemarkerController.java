package cn.lllllan.demo.controller;

import cn.lllllan.demo.config.WXConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/freemarker")
public class FreemarkerController {

    @Autowired
    private WXConfig wxConfig;

    @GetMapping("test")
    public String index(ModelMap modelMap) {
        modelMap.addAttribute("setting", wxConfig);
        return "user/fm/index";
    }
}
