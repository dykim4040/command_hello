package com.exam.domain;


import java.util.Date;

import lombok.Data;

/*
Board 테이블 생성 SQL문

CREATE TABLE board (
    num INT PRIMARY KEY,            
    name VARCHAR(20),
    pass VARCHAR(15),
    subject VARCHAR(50),
    content VARCHAR(2000),
    filename VARCHAR(50),
    re_ref INT,
    re_lev INT,
    re_seq INT,
    readcount INT,
    reg_date TIMESTAMP
);

ALTER TABLE board ADD ip VARCHAR(30);
*/

@Data
public class BoardVO {
    private int num;         // 글번호
    private String name;     // 작성자명
    private String pass;     // 글 비밀번호
    private String subject;  // 글제목
    private String content;  // 글내용
    private String filename; // 업로드한 파일명
    private int reRef;      // 글그룹 번호
    private int reLev;      // 글 들여쓰기(답글) 레벨
    private int reSeq;      // 글그룹 내에서의 순서
    private int readcount;   // 조회수
    private Date regDate;  // 글 작성(등록) 날짜시간
    private String ip;       // 작성자 IP주소  
}



