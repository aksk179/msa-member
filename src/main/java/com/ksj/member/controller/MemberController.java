package com.ksj.member.controller;

import com.ksj.member.dto.EmailDTO;
import com.ksj.member.service.EmailService;
import com.ksj.member.service.MemberService;
import com.ksj.member.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class MemberController {

    @Autowired
    MemberService memberService;

    @Autowired
    EmailService emailService;

    @RequestMapping(value = "/member/member_list.page")
    public String selectMemberListPage(Model model) {
        List<MemberVO> memberList = memberService.selectMemberList(new MemberVO());
        model.addAttribute("memberList", memberList);
        return "/member/member_list";
    }

    @RequestMapping(value = "/login/member_register.page")
    public String registerMemberPage(Model model) {
        MemberVO memberVO = new MemberVO();
        model.addAttribute("member", memberVO);
        return "/login/member_register";
    }

    @RequestMapping(value = "/member_register.do")
    public String registerMember(@ModelAttribute MemberVO memberVO) {
        log.debug("memberVO : " + memberVO.toString());
        memberService.registerMember(memberVO);
        emailService.sendEmail(memberVO);
        log.debug("===================== 회원가입완료 ====================");
        return "/login/member_register_success";
    }

    @RequestMapping(value = "/member/update_member.page", method = RequestMethod.GET)
    public String updateMemberPage(@RequestParam("id") String id, Model model) {
        MemberVO memberVO = new MemberVO();
        memberVO.setId(id);
        MemberVO memberOne = memberService.selectMemberOne(memberVO);
        log.debug("memberOne : " + memberOne.toString());
        model.addAttribute("member", memberOne);
        return "/member/update_member";
    }

    @RequestMapping(value = "/member/update_member.do")
    public String updateMemberOne(@ModelAttribute MemberVO memberVO) {
        log.debug("memberVO : " + memberVO.toString());
        memberService.updateMemberOne(memberVO);
        log.debug("===================== 회원정보수정완료 ====================");
        return "/member/member_update_success";
    }

    @RequestMapping(value = "/login/member_login.page")
    public String loginMemberPage(Model model) {
        MemberVO memberVO = new MemberVO();
        model.addAttribute("member", memberVO);
        return "/login/member_login";
    }

    @RequestMapping(value = "/member_main.page")
    public String mainPage() {
        return "member_main";
    }
}
