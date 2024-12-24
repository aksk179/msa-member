package com.ksj.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class MemberApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(MemberApplication.class, args);
	}

}
