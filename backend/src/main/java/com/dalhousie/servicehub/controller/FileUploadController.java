package com.dalhousie.servicehub.controller;

import com.dalhousie.servicehub.exceptions.FileUploadException;
import com.dalhousie.servicehub.factory.service.ServiceFactory;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.response.FileUploadResponse;
import com.dalhousie.servicehub.service.file_upload.FileUploadService;
import com.dalhousie.servicehub.util.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.dalhousie.servicehub.util.ResponseBody.ResultType.FAILURE;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    private static final Logger logger = LogManager.getLogger(FileUploadController.class);
    private final FileUploadService fileUploadService;

    public FileUploadController(ServiceFactory serviceFactory) {
        fileUploadService = serviceFactory.getFileUploadService();
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseBody<FileUploadResponse>> handleFileUpload(
            @AuthenticationPrincipal UserModel userModel,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            logger.info("Uploading file: {}", file.getOriginalFilename());
            ResponseBody<FileUploadResponse> body = fileUploadService.saveFile(userModel.getId(), file);
            logger.info("File uploaded successfully: {}", body.data().getUrl());
            return ResponseEntity.ok(body);
        } catch (FileUploadException exception) {
            logger.error("Failed to upload file: {}", exception.getMessage());
            ResponseBody<FileUploadResponse> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.badRequest().body(body);
        } catch (Exception exception) {
            logger.error("Error occurred while uploading file: {}", exception.getMessage());
            ResponseBody<FileUploadResponse> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.badRequest().body(body);
        }
    }
}
