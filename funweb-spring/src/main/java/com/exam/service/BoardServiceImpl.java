package com.exam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exam.domain.AttachVO;
import com.exam.domain.BoardVO;
import com.exam.mapper.AttachMapper;
import com.exam.mapper.BoardMapper;

import lombok.Setter;

@Service
public class BoardServiceImpl implements BoardService {

    @Setter(onMethod_ = @Autowired)
    private BoardMapper mapper;
    
    @Setter(onMethod_ = @Autowired)
    private AttachMapper attachMapper;
    
    
    @Override
    public int getSeqBoardNum() {
        return mapper.getSeqBoardNum();
    } 
    
    @Override
    public int insert(BoardVO boardVO) {
        
        
        return mapper.insertBoard(boardVO); // 주글 등록
    }

    @Override
    public List<BoardVO> getBoards(int pageNum, int amount, String search) {
        return mapper.getBoards(pageNum, amount, search);
    }

    @Override
    public int getBoardCount(String search) {
        return mapper.getBoardCount(search);
    }

    @Override
    public int updateReadcount(int num) {
        return mapper.updateReadcount(num);
    }

    @Override
    public BoardVO getBoard(int num) {
        return mapper.getBoard(num);
    }

    @Override
    public boolean updateBoard(BoardVO board) {
        boolean isSuccess = false;
        
        BoardVO dbBoard = mapper.getBoard(board.getNum());
        
        if (board.getPass().equals(dbBoard.getPass())) {
            mapper.updateBoard(board);
            isSuccess = true;
        } else {
            isSuccess = false;
        }
        
        return isSuccess;
    }

    @Override
    public boolean deleteBoard(int num, String pass) {
        boolean isSuccess = false;
        
        BoardVO dbBoard = mapper.getBoard(num);
        
        if (pass.equals(dbBoard.getPass())) {
            mapper.deleteBoard(num);
            isSuccess = true;
        } else {
            isSuccess = false;
        }
        
        return isSuccess;
    }

    @Transactional
    @Override
    public void replyInsert(BoardVO board) {
        // 1) 기존 답글들의 순서(re_seq) 재배치
        mapper.updateReplyGroupSeq(board.getReRef(), board.getReSeq());
        
        
        int num = mapper.getSeqBoardNum(); // 글번호
        board.setNum(num);
        // re_lev 는 [답글을 다는 대상글]의 들여쓰기값 + 1
        board.setReLev(board.getReLev() + 1);
        // re_seq 는 [답글을 다는 대상글]의 글그룹 내 순번값 + 1
        board.setReSeq(board.getReSeq() + 1);
        // 답글 조회수 0
        board.setReadcount(0);
        
        mapper.insertBoard(board);
    }

    @Transactional
    @Override
    public void insertBoardAndAttach(BoardVO board, List<AttachVO> attachList) {
        // 파일게시판 주글 등록
        mapper.insertBoard(board);
        
        if (attachList.size() > 0) { // 첨부파일 있으면
            for (AttachVO attachVO : attachList) {
                // 첨부파일 정보 등록
                attachMapper.insert(attachVO);
            }
        }
    } // insertBoardAndAttach()

    @Override
    public List<AttachVO> findByBnum(int bnum) {
        return attachMapper.findByBnum(bnum);
    }

    @Transactional
    @Override
    public void deleteBoardAndAttach(int num) {
        mapper.deleteBoard(num);
//        attachMapper.
    }

}




