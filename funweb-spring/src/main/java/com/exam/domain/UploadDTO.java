package com.exam.domain;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UploadDTO {
   // private MultipartFile file1, file2, file3, file4, file5;
    
    private MultipartFile[] files;
}
