package com.ksj.member.vo;

import lombok.Data;

@Data
public class MemberVO {
    private String id;
    private String name;
    private String passwd;
    private String newPasswd;
    private String cellPhone;
    private String email;
    private String zipCode;
    private String address;
    private String status;
    private String uuid;
}
