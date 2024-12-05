package com.ksj.member.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmailDTO {
    private String id;
    private String sendAt;
    private String uuid;
    private String receiver;
}
