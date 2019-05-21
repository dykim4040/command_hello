package com.exam.mapper;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.exam.domain.MemberVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class MemberMapperTests {
    @Autowired
    private MemberMapper mapper;
    
    @Test
    public void testInsert() {
        MemberVO memberVO = new MemberVO();
        memberVO.setId("iii");
        memberVO.setPassword("1234");
        memberVO.setName("홍길동");
        memberVO.setGender("남");
        
        int count = mapper.insert(memberVO);
        
        log.info(memberVO);
        log.info("insert한 행 개수: " + count);
    }
    
    @Test
    public void testGetList() {
        List<MemberVO> list = mapper.getList();
        for (MemberVO memberVO : list) {
            log.info(memberVO);
        }
    }
    @Test
    public void testGetMemberById() {
        MemberVO memberVO = mapper.getMemberById("aaa");
        log.info(memberVO);
    }
    @Test
    public void testDelete() {
        int count = mapper.delete("aaa");
        log.info("delete한 행 개수: " + count);
    }
    @Test
    public void testUpdate() {
        MemberVO memberVO = mapper.getMemberById("aaa");
        memberVO.setPassword("9999");
        memberVO.setName("박보검");
        
        int count = mapper.update(memberVO);
        log.info("update한 행 개수: " + count);
    }
    @Test
    public void testCountById() {
        int count = mapper.countById("aaa");
        log.info("카운트한 행 개수: " + count);
    }
    
}






