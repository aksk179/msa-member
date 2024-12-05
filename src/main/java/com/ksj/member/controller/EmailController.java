package com.ksj.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksj.member.dto.EmailDTO;
import com.ksj.member.service.EmailService;
import com.ksj.member.util.AESUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@Slf4j
public class EmailController {

    @Autowired
    EmailService emailService;

    @RequestMapping(value = "/verify-email.do", method = RequestMethod.GET)
    public String verifyEmail(@RequestParam("token") String token) {
        return emailService.verifyEmail(token);
    }
}
