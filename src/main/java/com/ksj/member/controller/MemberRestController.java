package com.ksj.member.controller;

import com.ksj.member.service.MemberService;
import com.ksj.member.vo.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class MemberRestController {

    @Autowired
    MemberService memberService;

    @RequestMapping(value = "/get_member_list.do", method = RequestMethod.POST)
    public List<MemberVO> selectMemberList(MemberVO memberVO) {
        return memberService.selectMemberList(memberVO);
    }

    @RequestMapping(value = "/check_duplicate.do", method = RequestMethod.POST)
    public ResponseEntity<String> isDuplicate(@RequestParam("id") String id) {
        MemberVO memberVO = new MemberVO();
        memberVO.setId(id);
        return ResponseEntity.ok(memberService.isDuplicate(memberVO));
    }

}
