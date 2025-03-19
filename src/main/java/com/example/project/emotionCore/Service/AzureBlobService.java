package com.example.project.emotionCore.Service;

import com.azure.storage.blob.*;
import com.azure.storage.blob.sas.BlobContainerSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.azure.storage.common.sas.SasProtocol;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class AzureBlobService {
    private static final String ACCOUNT_NAME = "emotioncore";
    private static final String ACCOUNT_KEY = "tQywPGfcRzmg0zyS95RJULJct2c1CPXAFPd28y2at1xrAe4SwlittwdiKNLKGHo4KhwtgiB+bdp3+AStdlpoAg==";
    private static final StorageSharedKeyCredential CREDENTIAL = new StorageSharedKeyCredential(ACCOUNT_NAME, ACCOUNT_KEY);
    private static final String STORAGE_NAME = "https://drive.emotioncores.com";
    private static final BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
            .endpoint(STORAGE_NAME)
            .credential(CREDENTIAL)
            .buildClient();
    private static final String CONTAINER_NAME = "main";
    private static final BlobContainerClient CONTAINER = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);
    private static final BlobContainerSasPermission WRITE_UPDATE_PERMISSION
            = new BlobContainerSasPermission().setWritePermission(true);


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
