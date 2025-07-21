package com.example.project.emotionCore.service;

import com.azure.storage.blob.*;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.example.project.emotionCore.domain.Episode;
import com.example.project.emotionCore.exception.CustomBadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class AzureBlobService {
    private final String ACCOUNT_NAME;
    private final String ACCOUNT_KEY;
    private final StorageSharedKeyCredential CREDENTIAL;
    private final String STORAGE_NAME;
    private final BlobServiceClient blobServiceClient;
    private final String CONTAINER_NAME;
    private final BlobContainerClient CONTAINER;
    private final ImageValidatorService imageValidatorService;

    @Autowired
    public AzureBlobService(
            @Value("${azure.account.name}") String accountName,
            @Value("${azure.account.key}") String accountKey,
            @Value("${azure.account.storage.url}") String storageUrl,
            @Value("${azure.account.container.name}") String containerName,
            ImageValidatorService imageValidatorService) {
        this.ACCOUNT_NAME = accountName;
        this.ACCOUNT_KEY = accountKey;
        this.STORAGE_NAME = storageUrl;
        this.CREDENTIAL = new StorageSharedKeyCredential(ACCOUNT_NAME, ACCOUNT_KEY);
        this.blobServiceClient = new BlobServiceClientBuilder()
                                    .endpoint(STORAGE_NAME)
                                    .credential(CREDENTIAL)
                                    .buildClient();
        this.CONTAINER_NAME = containerName;
        this.CONTAINER = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);
        this.imageValidatorService = imageValidatorService;
    }
/*
    public String generateContainerSasToken(){
        OffsetDateTime expireTime = OffsetDateTime.now().plusMinutes(5);
        BlobServiceSasSignatureValues signatureValues
                = new BlobServiceSasSignatureValues(expireTime, WRITE_UPDATE_PERMISSION)
                .setProtocol(SasProtocol.HTTPS_ONLY)
                .setStartTime(OffsetDateTime.now().minusMinutes(3));
        
        return CONTAINER.generateSas(signatureValues);
    }

    public void updateFileNames(Map<String, String> nameList){
        nameList.forEach(this::updateFileName);
    }

    private void updateFileName(String from, String to){
        BlobClient oldFile = CONTAINER.getBlobClient(from);
        BlobClient newFile = CONTAINER.getBlobClient(to);
        newFile.beginCopy(oldFile.getBlobUrl(), Duration.ofSeconds(2));
        oldFile.delete();
    }
*/
    public void uploadImages(Episode.EpisodeKey episodeKey, List<MultipartFile> multipartFiles){
        int count = 0;
        for(MultipartFile multipartFile : multipartFiles){
            if(multipartFile == null || multipartFile.isEmpty()) continue;
            String extension = imageValidatorService.getExtension(multipartFile);
            String fileName = "https://drive.emotioncores.com/main/"+episodeKey.getSeriesId() + "/" + episodeKey.getNumber() + "/" + count + extension;
            uploadImage(fileName, multipartFile);
            count++;
        }
    }

    void uploadImage(String filename, MultipartFile multipartFile){
        if(multipartFile == null || multipartFile.isEmpty()) return;
        BlobClient file = CONTAINER.getBlobClient(filename);
        try {
            file.upload(multipartFile.getInputStream(), true);
        }
        catch (IOException e){
            throw new CustomBadRequestException(500, "파일 업로드 에러 : "+multipartFile.getOriginalFilename());
        }
    }

    public void uploadImage(String filename, InputStream inputStream){
        BlobClient file = CONTAINER.getBlobClient(filename);
        file.upload(inputStream, true);
    }

    public InputStream getDefaultImage(){
        BlobClient file = CONTAINER.getBlobClient("default.png");
        return file.openInputStream();
    }



    /* test code
    public BlobContainerClient getContainerClient(){
        return new BlobContainerClientBuilder()
                .endpoint(STORAGE_NAME)
                .containerName(CONTAINER_NAME)
                .sasToken(generateContainerSasToken())
                .buildClient();
    }

    public void TestForUploadFile(Long id, MultipartFile file) throws IOException {
        BlobContainerClient newCon = getContainerClient();
        BlobClient client = newCon.getBlobClient("TestFile9.txt"); //이거 바꾸고ㅇ
        client.upload(file.getInputStream(), true);
    }

    public void TestForChangeFileName(){
        BlobClient oldFile = CONTAINER.getBlobClient("TestFile3.txt");
        BlobClient newFile = CONTAINER.getBlobClient("TESTFILE123.txt");
        System.out.println("oldFile: " + oldFile.getBlobUrl());
        newFile.copyFromUrl(oldFile.getBlobUrl());
        oldFile.delete();
    }
    */
}
