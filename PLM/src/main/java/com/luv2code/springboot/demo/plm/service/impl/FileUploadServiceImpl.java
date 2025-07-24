package com.luv2code.springboot.demo.plm.service.impl;

import com.luv2code.springboot.demo.plm.exception.FileUploadException;
import com.luv2code.springboot.demo.plm.service.FileUploadService;
import com.luv2code.springboot.demo.plm.util.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {

    private static final String UPLOAD_DIR = "uploads";
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    @Override
    public String uploadFile(MultipartFile file, String subDirectory) {
        if (file.isEmpty()) {
            throw new FileUploadException("File không được để trống");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileUploadException("File quá lớn. Kích thước tối đa là 10MB");
        }

        if (!FileUtils.isImageFile(file)) {
            throw new FileUploadException("Chỉ chấp nhận file ảnh (JPG, PNG, GIF)");
        }

        try {
            String fileName = generateFileName(file.getOriginalFilename());
            Path uploadPath = Paths.get(UPLOAD_DIR, subDirectory);

            // Tạo thư mục nếu chưa tồn tại
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            log.info("Uploaded file: {}", filePath);
            return fileName;

        } catch (IOException e) {
            log.error("Failed to upload file", e);
            throw new FileUploadException("Không thể upload file: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteFile(String fileName, String subDirectory) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR, subDirectory, fileName);
            boolean deleted = Files.deleteIfExists(filePath);

            if (deleted) {
                log.info("Deleted file: {}", filePath);
            }

            return deleted;
        } catch (IOException e) {
            log.error("Failed to delete file: {}", fileName, e);
            return false;
        }
    }

    private String generateFileName(String originalFileName) {
        String extension = FileUtils.getFileExtension(originalFileName);
        return UUID.randomUUID().toString() + "." + extension;
    }
}