package com.ksj.member.service;

import com.ksj.member.vo.MemberVO;

public interface EmailService {
    public void sendEmail(MemberVO memberVO);

    public String verifyEmail(String token);
}
