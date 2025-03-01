package com.gym.server.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface FileService {
    String convertFileToBase64(MultipartFile file);
    void saveBase64ToFile(String base64, String fileName);
    String showProfileBase64(String fileName);

}
