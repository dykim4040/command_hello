package com.exam.mapper;

import java.util.List;

import com.exam.domain.AttachVO;

public interface AttachMapper {
    
    public void insert(AttachVO attachVO);
    
    public void delete(String uuid);
    
    public void deleteByBoardNum(int bnum);
    
    public List<AttachVO> findByBnum(int bnum);
}
