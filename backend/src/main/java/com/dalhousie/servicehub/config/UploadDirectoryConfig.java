package com.dalhousie.servicehub.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class UploadDirectoryConfig {

    private final String uploadPath;

    public UploadDirectoryConfig(@Value("${upload.path}") String uploadPath) {
        this.uploadPath = uploadPath;
    }

    @PostConstruct
    public void init() {
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            uploadDir.mkdirs();
        }
    }
}
