package com.luv2code.springboot.demo.plm.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    String uploadFile(MultipartFile file, String subDirectory);
    boolean deleteFile(String fileName, String subDirectory);
}