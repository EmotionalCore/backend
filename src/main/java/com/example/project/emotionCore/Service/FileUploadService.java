package com.example.project.emotionCore.Service;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class FileUploadService {
    @Value("${file.upload.dir}")
    private String uploadDir;

    private final Tika tika = new Tika();
    private final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif");

    public List<String> storeFiles(List<MultipartFile> files) throws IOException {
        if(files == null || files.isEmpty()) {
            return null;
        }
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            fileNames.add(storeFile(file));
        }
        return fileNames;
    }

    public String storeFile(MultipartFile file) throws IOException {
        if(!validateFile(file)){
            return null;
        }
        String fileExtension = getFileExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID() + "." + fileExtension;

        Path targetLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(targetLocation);
        Path filePath = targetLocation.resolve(fileName);

        Files.write(filePath, file.getBytes());
        return fileName;

    }

    public boolean validateFile(MultipartFile file) {
        if(file == null || file.isEmpty()) {
            return false;
        }
        String mimeType = tika.detect(file.getOriginalFilename());
        return ALLOWED_EXTENSIONS.contains(mimeType); //조건들 추가 가능
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new IllegalArgumentException("올바른 파일명을 입력하세요.");
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }
}
