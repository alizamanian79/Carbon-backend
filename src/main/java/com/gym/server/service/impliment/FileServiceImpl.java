package com.gym.server.service.impliment;

import com.gym.server.service.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
@AllArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    @Override
    public String convertFileToBase64(MultipartFile file) {
        try {
            // Convert the file to Base64
            byte[] fileBytes = file.getBytes();
            String base64File = Base64.getEncoder().encodeToString(fileBytes);
            return base64File;
        } catch (IOException e) {
            // Log the error and handle it appropriately
            log.error("Error converting file to Base64: {}", e.getMessage());
            throw new RuntimeException("Failed to convert file to Base64", e);
        }
    }

    @Override
    public void saveBase64ToFile(String base64, String fileName) {
        try {
            // Decode the Base64 string to bytes
            byte[] fileBytes = Base64.getDecoder().decode(base64);

            // Define the directory path
            String directoryPath = "src/main/resources/static/uploads/profileImages";
            File directory = new File(directoryPath);

            // Create the directory if it doesn't exist
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Create the file and write the bytes
            String filePath = directoryPath + File.separator + fileName;
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(fileBytes);
            }

            log.info("File saved successfully: {}", filePath);
        } catch (IOException e) {
            log.error("Error saving Base64 to file: {}", e.getMessage());
            throw new RuntimeException("Failed to save Base64 to file", e);
        }
    }
}
