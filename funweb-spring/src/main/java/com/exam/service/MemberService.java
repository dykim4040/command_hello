package com.exam.service;

import java.util.List;

import com.exam.domain.MemberVO;

public interface MemberService {
    
    public int register(MemberVO memberVO);
    
    public boolean isIdDuplicated(String id);
    
    public int loginCheck(String id, String pass);
    
    public List<MemberVO> getAllMembers();
    
}



