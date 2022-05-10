package cn.lllllan.ticket.service;

public interface VerifyCodeService {

    public Object sendVerifyCode(String to);

    public Object checkVerifyCode(String email, String code);
}
