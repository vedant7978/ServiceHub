package com.dalhousie.servicehub.service.file_upload;

import com.dalhousie.servicehub.response.FileUploadResponse;
import com.dalhousie.servicehub.util.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

    /**
     * Stores the file on the server and provides URL for the uploaded file
     * @param userId ID of the user
     * @param file File to store in server
     * @return ResponseBody for FileUploadResponse
     */
    ResponseBody<FileUploadResponse> saveFile(Long userId, MultipartFile file);
}
