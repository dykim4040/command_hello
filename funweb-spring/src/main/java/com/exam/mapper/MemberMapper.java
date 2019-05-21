package com.exam.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.exam.domain.MemberVO;

public interface MemberMapper {

    public int insert(MemberVO member);
    
    //@Select("SELECT * FROM member ORDER BY id")
    public List<MemberVO> getList();
    
    public MemberVO getMemberById(String id);
    
    public int delete(String id);
    
    public int update(MemberVO member);
    
    public int countById(String id);
    
}




