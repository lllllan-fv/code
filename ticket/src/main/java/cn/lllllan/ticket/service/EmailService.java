package cn.lllllan.ticket.service;

public interface EmailService {

    public void sendMail(String to, String subject, String content);
}
