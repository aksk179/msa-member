package com.ksj.member.service;

import com.ksj.member.mapper.MemberMapper;
import com.ksj.member.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService{

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

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
        String token = UUID.randomUUID().toString();
        memberVO.setUuid(token);
        memberVO.setStatus("N");
        memberVO.setRole("USER");

        String encPwd = passwordEncoder.encode(memberVO.getPasswd());
        memberVO.setPasswd(encPwd);
        memberMapper.registerMember(memberVO);
    }

    @Override
    public void updateMemberOne(MemberVO memberVO) {
        String encPwd = passwordEncoder.encode(memberVO.getNewPasswd());
        memberVO.setNewPasswd(encPwd);
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

        String originalPwd = passwordEncoder.encode(memberVO.getPasswd());

        log.info("기존 비밀번호 : " + returnMem.getPasswd());

        log.info("입력한 비밀번호 : " + memberVO.getPasswd());
        log.info("입력한 비밀번호 암호화 : " + originalPwd);


        if (passwordEncoder.matches(memberVO.getPasswd(), returnMem.getPasswd())) {
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
