package com.dalhousie.servicehub.service.file_upload;

import com.dalhousie.servicehub.exceptions.FileUploadException;
import com.dalhousie.servicehub.response.FileUploadResponse;
import com.dalhousie.servicehub.util.FileHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private final String uploadPath;
    private final FileHelper fileHelper;

    public FileUploadServiceImpl(@Value("${upload.path}") String uploadPath, FileHelper fileHelper) {
        this.uploadPath = uploadPath;
        this.fileHelper = fileHelper;
    }

    @Override
    public FileUploadResponse saveFile(Long userId, MultipartFile file) {
        if (file.isEmpty())
            throw new FileUploadException("Cannot upload file because provided file is empty");

        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isBlank())
            throw new FileUploadException("Cannot upload file because provided file name is empty");

        try {
            String fileUrl = saveAndGetPublicUrlForFile(userId, file, fileName);
            return FileUploadResponse.builder().url(fileUrl).build();
        } catch (IOException exception) {
            throw new FileUploadException(exception.getMessage());
        }
    }

    /**
     * Provides publicly accessed URL for the requesting file
     * @param userId ID of the user
     * @param file Requesting file to get public url
     * @param fileName Name of the file
     * @return String representing public URL for the file
     * @throws IOException if error occurs while reading bytes from the file
     */
    private String saveAndGetPublicUrlForFile(Long userId, MultipartFile file, String fileName) throws IOException {
        String extension = "";
        int index = fileName.lastIndexOf(".");
        if (index != -1)
            extension = fileName.substring(index);
        Path path = Paths.get(uploadPath + File.separator + userId + extension);
        fileHelper.write(path, file.getBytes());
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/public/uploads/" + userId + extension)
                .toUriString();
    }
}
