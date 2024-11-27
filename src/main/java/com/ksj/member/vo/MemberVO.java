package com.ksj.member.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
//@Getter
//@Setter
public class MemberVO {
    private String id;
    private String name;
    private String passwd;
    private String cellPhone;
    private String email;
    private String zipCode;
    private String address;
    public String status;
}
