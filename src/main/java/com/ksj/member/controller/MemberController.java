package com.ksj.member.controller;

import com.ksj.member.service.MemberService;
import com.ksj.member.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class MemberController {

    @Autowired
    MemberService memberService;

    @RequestMapping(value = "/member_list.page")
    public String selectMemberListPage(Model model) {
        List<MemberVO> memberList = memberService.selectMemberList(new MemberVO());
        model.addAttribute("memberList", memberList);
        return "member_list";
    }

    @RequestMapping(value = "/member_register.page")
    public String registerMemberPage(Model model) {
        MemberVO memberVO = new MemberVO();
        model.addAttribute("member", memberVO);
        return "member_register";
    }

    @RequestMapping(value = "/member_register.do")
    public String registerMember(@ModelAttribute MemberVO memberVO) {
        log.debug("memberVO : " + memberVO.toString());
        memberService.registerMember(memberVO);
        log.debug("===================== 회원가입완료 ====================");
        return "member_register_success";
    }
}
