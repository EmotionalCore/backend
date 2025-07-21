package com.example.project.emotionCore.service;

import com.example.project.emotionCore.domain.Episode;
import com.example.project.emotionCore.exception.CustomBadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private final AzureBlobService azureBlobService;
    private final ImageValidatorService imageValidatorService;

    public void uploadImageToCloud(String filename, MultipartFile image){
        if(image == null || image.isEmpty()){
            InputStream inputStream = azureBlobService.getDefaultImage();
            azureBlobService.uploadImage(filename, inputStream);
            return;
        }
        checkImagesSecurity(Collections.singletonList(image));
        azureBlobService.uploadImage(filename, image);
    }

    public void uploadImagesToCloud(Episode.EpisodeKey episodeKey, List<MultipartFile> images){
        checkImagesSecurity(images);
        azureBlobService.uploadImages(episodeKey, images);
    }

    private void checkImagesSecurity(List<MultipartFile> images) {
        for (MultipartFile image : images) {
            if (!imageValidatorService.isValidImage(image)) {
                throw new CustomBadRequestException(400, "Invalid image");
            }
        }
    }
}

