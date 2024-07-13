package com.puppypaws.project.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AwsS3UploadService {

    private final AmazonS3Client amazonS3;

    @Value("${spring.aws.s3.bucketName}")
    private String bucket;


    public String saveFile(MultipartFile multipartFile, String folderName) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        String endPoint = bucket + "/" + folderName;

        amazonS3.putObject(endPoint, originalFilename, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(endPoint, originalFilename).toString();
    }
}
