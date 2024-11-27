package com.ksj.member.mapper;

import com.ksj.member.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {
    public List<MemberVO> selectMemberList(MemberVO memberVO);
}
