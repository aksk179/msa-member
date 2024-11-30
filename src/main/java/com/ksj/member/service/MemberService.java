package com.ksj.member.service;

import com.ksj.member.vo.MemberVO;

import java.util.List;

public interface MemberService {
    public List<MemberVO> selectMemberList(MemberVO memberVO);

    public MemberVO selectMemberOne(MemberVO memberVO);

    public void registerMember(MemberVO memberVO);

    public String isDuplicate(MemberVO memberVO);

    public void updateMemberOne(MemberVO memberVO);

    public String chkPasswd(MemberVO memberVO);

    public String loginMember(MemberVO memberVO);
}
