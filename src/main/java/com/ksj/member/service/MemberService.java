package com.ksj.member.service;

import com.ksj.member.vo.MemberVO;

import java.util.List;

public interface MemberService {
    public List<MemberVO> selectMemberList(MemberVO memberVO);
}
