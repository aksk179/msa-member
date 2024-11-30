package com.ksj.member.service;

import com.ksj.member.mapper.MemberMapper;
import com.ksj.member.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService{

    @Autowired
    MemberMapper memberMapper;

    @Override
    public List<MemberVO> selectMemberList(MemberVO memberVO) {
        return memberMapper.selectMemberList(memberVO);
    }

    @Override
    public MemberVO selectMemberOne(MemberVO memberVO) {
        return memberMapper.selectMemberOne(memberVO);
    }

    @Override
    public void registerMember(MemberVO memberVO) {
        memberVO.setStatus("N");
        memberMapper.registerMember(memberVO);
    }

    @Override
    public void updateMemberOne(MemberVO memberVO) {
        memberMapper.updateMemberOne(memberVO);
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


    @Override
    public String chkPasswd(MemberVO memberVO) {
        MemberVO returnMem = memberMapper.chkPasswd(memberVO);
        log.debug("입력한 비밀번호 : " + memberVO.getPasswd());
        log.debug("기존 비밀번호 : " + returnMem.getPasswd());

        if (memberVO.getPasswd().equals(returnMem.getPasswd())) {
            return "Y";
        } else {
            return "N";
        }
    }

    @Override
    public String loginMember(MemberVO memberVO) {
        int cnt = memberMapper.loginMember(memberVO);

        if (cnt > 0) {
            return "Y";
        } else {
            return "N";
        }
    }
}
