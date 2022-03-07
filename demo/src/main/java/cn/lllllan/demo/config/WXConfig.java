package cn.lllllan.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:pay.properties")
public class WXConfig {

    @Value("${wxpay.appid}")
    private String payAppId;

    @Value("${wxpay.secret}")
    private String paySecret;

    @Value("${wxpay.mechid}")
    private String payMechId;

    public String getPayAppId() {
        return payAppId;
    }

    public void setPayAppId(String payAppId) {
        this.payAppId = payAppId;
    }

    public String getPaySecret() {
        return paySecret;
    }

    public void setPaySecret(String paySecret) {
        this.paySecret = paySecret;
    }

    public String getPayMechId() {
        return payMechId;
    }

    public void setPayMechId(String payMechId) {
        this.payMechId = payMechId;
    }
}
