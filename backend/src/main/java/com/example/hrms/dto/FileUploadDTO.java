package com.example.hrms.dto;

import lombok.Data;
import java.time.LocalDateTime;

// 文件上传DTO
@Data
public class FileUploadDTO {
    private String fileName;
    private String filePath;
    private String fileType;
    private Long fileSize;
    private String uploadTime;
    private String uploadUser;
}