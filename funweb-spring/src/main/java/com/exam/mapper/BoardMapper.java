package com.exam.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.exam.domain.BoardVO;

public interface BoardMapper {
    
    public int getSeqBoardNum();

    public int insertBoard(BoardVO boardVO);
    
    public List<BoardVO> getBoards(@Param("pageNum") int pageNum, 
            @Param("amount") int amount, 
            @Param("search") String search);
    
    public int getBoardCount(@Param("search") String search);
    
    public int updateReadcount(int num);
    
    public BoardVO getBoard(int num);
    
    public int updateBoard(BoardVO board);
    
    public int deleteBoard(int num);
    
    public int updateReplyGroupSeq(@Param("reRef") int re_ref, @Param("reSeq") int re_seq);
    
   
    
}



