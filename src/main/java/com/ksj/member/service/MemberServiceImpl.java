package com.ksj.member.service;

import com.ksj.member.mapper.MemberMapper;
import com.ksj.member.vo.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService{

    @Autowired
    MemberMapper memberMapper;

    @Override
    public List<MemberVO> selectMemberList(MemberVO memberVO) {
        return memberMapper.selectMemberList(memberVO);
    }

    @Override
    public void registerMember(MemberVO memberVO) {
        memberVO.setStatus("N");
        memberMapper.registerMember(memberVO);
    }

    @Override
    public String isDuplicate(MemberVO memberVO) {
        int cnt = memberMapper.isDuplicate(memberVO);

        if (cnt > 0) {
            return "Y";
        } else {
            return "N";
        }
    }
}