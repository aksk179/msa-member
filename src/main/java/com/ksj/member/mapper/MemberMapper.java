package com.ksj.member.mapper;

import com.ksj.member.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {
    public List<MemberVO> selectMemberList(MemberVO memberVO);

    public MemberVO selectMemberOne(MemberVO memberVO);

    public void registerMember(MemberVO memberVO);

    public int isDuplicate(MemberVO memberVO);

    public void updateMemberOne(MemberVO memberVO);

    public MemberVO chkPasswd(MemberVO memberVO);

    public int loginMember(MemberVO memberVO);

    public MemberVO selectMemberUuid(MemberVO memberVO);

    public MemberVO selectMemberPasswd(MemberVO memberVO);
}
