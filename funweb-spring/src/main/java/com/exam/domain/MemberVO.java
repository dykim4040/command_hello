package com.exam.domain;


import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

/*
Member 테이블 생성 SQL문

CREATE TABLE member (
    id VARCHAR(15) PRIMARY KEY,
    password VARCHAR(15) NOT NULL,
    name VARCHAR(15),
    birthday TIMESTAMP,
    gender VARCHAR(5),
    email VARCHAR(30),
    address VARCHAR(60),
    tel VARCHAR(20),
    mtel VARCHAR(20),
    reg_date TIMESTAMP
);
*/
@Data
public class MemberVO {
	private String id;
	private String password;
	private String name;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthday;
	
	private String gender;
	private String email;
	private String address;
	private String tel;
	private String mtel;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date regDate;
}
