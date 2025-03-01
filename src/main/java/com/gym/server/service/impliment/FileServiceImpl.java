package com.gym.server.service.impliment;

import com.gym.server.service.FileService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class FileServiceImpl implements FileService {

    @Value("${app.path.profileImages}")
    private String profileDir;


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
            String directoryPath = profileDir;
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

    @Override
    public String showProfileBase64(String fileName) {
        try {

            // Define the file path
            String filePath = profileDir+"/"+fileName;
            File file = new File(filePath);

            // Check if the file exists
            if (!file.exists()) {
                return "File not found: " + filePath;
            }

            // Read the file and convert it to Base64
            byte[] fileBytes = new byte[(int) file.length()];
            try (FileInputStream fis = new FileInputStream(file)) {
                fis.read(fileBytes);
            }

            String base64Image = Base64.getEncoder().encodeToString(fileBytes);
            return base64Image;
        } catch (IOException e) {
            return "Error reading file: " + e.getMessage();
        }
    }

    @Override
    public String deleteProfileImage(String fileName) {
        File file = new File(profileDir +"/"+ fileName);
        if (file.exists()) {
            if (file.delete()) {
                return "File " + fileName + " deleted successfully.";
            } else {
                return "Failed to delete the file " + fileName + ".";
            }
        } else {
            return "File " + fileName + " does not exist.";
        }
    }

}
