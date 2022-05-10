package cn.lllllan.ticket.service.impl;

import cn.lllllan.ticket.service.EmailService;
import cn.lllllan.ticket.utils.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private EmailUtil emailUtil;


    public void sendMail(String to, String subject, String content) {
        emailUtil.sendSimpleTextMailActual(subject, content, new String[]{to}, null, null, null);
    }
}
