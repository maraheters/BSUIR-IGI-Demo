package com.sparkplug.sparkplugbackend.minio.service;

import com.sparkplug.sparkplugbackend.exception.UnsupportedMediaTypeException;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class MinioFileService {

    private final MinioClient minioClient;
    private final String bucketName;

    public MinioFileService(
            MinioClient minioClient,
            @Value("${minio-bucket-name}") String bucketName) {
        this.minioClient = minioClient;
        this.bucketName = bucketName;
    }

    public byte[] downloadFile(String objectName) {
        try {
            InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());

            return stream.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("Error downloading file from bucket '" + bucketName + "':", e);
        }
    }

    public String uploadFile(MultipartFile file, String directory) {
        String originalFilename = file.getOriginalFilename();
        if(!isFileNameValid(originalFilename)) {
            throw new UnsupportedMediaTypeException(
                    "Filename must be valid and have .jpg or .png extension: " + originalFilename);
        }
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = directory + "/" + UUID.randomUUID() + extension;

        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .build());

            String objectUrl = "http://127.0.0.1:9000/" + bucketName + "/" + fileName;

            return objectUrl;
        } catch (Exception e) {
            throw new RuntimeException("Error uploading file to bucket '" + bucketName + "':" + e.getMessage(),e);
        }
    }

    public List<String> uploadFiles(List<MultipartFile> files, String directory) {
        List<CompletableFuture<String>> futures = files.stream()
                .map(file -> CompletableFuture.supplyAsync(() -> {
                    try {
                        return uploadFile(file, directory);
                    } catch (Exception e) {
                        throw new RuntimeException("Error uploading one of the files: " + e.getMessage(), e);
                    }
                }))
                .toList();

        // Wait for all uploads to complete and collect the results
        return futures.stream()
                .map(CompletableFuture::join) // This will block until each future completes
                .collect(Collectors.toList());

    }

    public void deleteFiles(List<String> objectUrls) {
        try {
            for (String url : objectUrls) {
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(bucketName)
                                .object(extractObjectNameFromUrl(url))
                                .build()
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting files from bucket '" + bucketName + "':", e);
        }
    }

    private String extractObjectNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("sparkplug/") + 1);
    }

    private boolean isFileNameValid(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }
        return fileName.endsWith(".png") || fileName.endsWith(".jpg");
    }
}
