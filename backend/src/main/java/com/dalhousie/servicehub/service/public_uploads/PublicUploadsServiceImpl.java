package com.dalhousie.servicehub.service.public_uploads;

import com.dalhousie.servicehub.util.UrlResourceHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PublicUploadsServiceImpl implements PublicUploadsService {

    private final String uploadPath;
    private final UrlResourceHelper urlResourceHelper;

    public PublicUploadsServiceImpl(@Value("${upload.path}") String uploadPath, UrlResourceHelper urlResourceHelper) {
        this.uploadPath = uploadPath;
        this.urlResourceHelper = urlResourceHelper;
    }

    @Override
    public Resource getFile(String filename) throws FileNotFoundException {
        Path filePath = Paths.get(uploadPath).resolve(filename).normalize();
        Resource resource = urlResourceHelper.getUrlResource(filePath.toUri());
        if (resource == null  || !resource.exists() || !resource.isReadable())
            throw new FileNotFoundException("File not found: " + filename);
        return resource;
    }
}
