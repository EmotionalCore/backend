package com.example.project.emotionCore.service;

import com.example.project.emotionCore.exception.CustomBadRequestException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageValidatorService {

    public boolean isValidImage(MultipartFile file) {
        return true;
    }

    public String getExtension(MultipartFile file) {
        return "";

    }
}
