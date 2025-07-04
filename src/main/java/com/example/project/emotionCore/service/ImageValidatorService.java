package com.example.project.emotionCore.service;

import com.example.project.emotionCore.exception.CustomBadRequestException;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageValidatorService {
    private static final Tika tika = new Tika();

    public boolean isValidImage(MultipartFile file) {
        try {
            String mimeType = tika.detect(file.getInputStream());
            return mimeType.equals("image/jpeg") || mimeType.equals("image/png");
        } catch (IOException e) {
            throw new CustomBadRequestException(400, "파일 업로드 에러 : " + file.getOriginalFilename());
        }
    }

    public String getExtension(MultipartFile file) {
        try {
            String mimeType = tika.detect(file.getInputStream());
            return MimeTypes.getDefaultMimeTypes().forName(mimeType).getExtension();
        } catch (MimeTypeException e) {
            throw new CustomBadRequestException(400, "알맞지 않은 확장자입니다.");
        } catch (IOException e) {
            throw new CustomBadRequestException(400, "파일 읽기 에러");
        }

    }
}
