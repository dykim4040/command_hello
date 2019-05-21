package com.exam.domain;

import lombok.Data;

@Data
public class AttachVO {
    private String uuid;
    private String uploadpath;
    private String filename;
    private String filetype;
    private int bnum;
    
    private boolean enabled; // default value is false
}
