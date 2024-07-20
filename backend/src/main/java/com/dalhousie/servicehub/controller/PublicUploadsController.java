package com.dalhousie.servicehub.controller;

import com.dalhousie.servicehub.service.public_uploads.PublicUploadsService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public")
public class PublicUploadsController {

    private static final Logger logger = LogManager.getLogger(PublicUploadsController.class);
    private final PublicUploadsService publicUploadsService;

    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try {
            Resource file = publicUploadsService.getFile(filename);
            logger.info("File retrieval successful: {}", filename);
            return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                        .body(file);
        } catch (FileNotFoundException exception) {
            logger.error("Failed to fetch file {}", exception.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception exception) {
            logger.error("Error fetching the file: {}", exception.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }
}
