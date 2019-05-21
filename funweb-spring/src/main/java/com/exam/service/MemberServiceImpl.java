package com.exam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exam.domain.MemberVO;
import com.exam.mapper.MemberMapper;

import lombok.Setter;

@Service
public class MemberServiceImpl implements MemberService {

    @Setter(onMethod_ = @Autowired)
    private MemberMapper mapper;

    //@Transactional
    @Override
    public int register(MemberVO memberVO) {
        return mapper.insert(memberVO);
    }

    @Override
    public boolean isIdDuplicated(String id) {
        int count = mapper.countById(id);
        
//      count == 1  아이디 있음. "사용중인 아이디 입니다."
//      0  아이디 없음. "사용가능한 ID입니다."

        boolean isDuplicated = false;
        if (count > 0) {
            isDuplicated = true;
        }
        
        return isDuplicated;
    }

    @Override
    public int loginCheck(String id, String pass) {
        // *반환값 의미
        // 아이디 불일치: -1
        // 아이디 일치, 비밀번호 불일치: 0
        // 아이디, 비밀번호 모두 일치: 1
        int check = -1;
        
        MemberVO memberVO = mapper.getMemberById(id);
        if (memberVO != null) {
            if (pass.equals(memberVO.getPassword())) {
                // 비밀번호 일치
                check = 1;
            } else {
                check = 0; // 비밀번호 불일치
            }
        } else {
            check = -1; // 아이디 불일치
        }
        
        return check;
    }

    @Override
    public List<MemberVO> getAllMembers() {
        return mapper.getList();
    }

}
