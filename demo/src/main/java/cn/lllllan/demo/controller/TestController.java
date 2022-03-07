package cn.lllllan.demo.controller;

import cn.lllllan.demo.config.WXConfig;
import cn.lllllan.demo.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pub/test")
@PropertySource({"classpath:pay.properties"})
public class TestController {

    @Value("${wxpay.appid}")
    private String payAppid;

    @Value("${wxpay.secret}")
    private String paySecret;

    @Autowired
    private WXConfig wxConfig;

    @GetMapping("config")
    public JsonData testConfig() {
        Map<String, String> map = new HashMap<>();
        map.put("appid", wxConfig.getPayAppId());
        map.put("secret", wxConfig.getPaySecret());
        return JsonData.buildSuccess(map);
    }
}
