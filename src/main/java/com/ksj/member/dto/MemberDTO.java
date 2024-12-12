package com.ksj.member.dto;

import lombok.Data;

@Data
public class MemberDTO {
    private String id;
    private String name;
    private String passwd;
    private String cellPhone;
    private String email;
    private String zipCode;
    private String address;
    private String status;
}
