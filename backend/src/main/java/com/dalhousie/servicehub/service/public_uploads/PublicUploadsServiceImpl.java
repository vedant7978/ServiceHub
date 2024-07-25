package com.dalhousie.servicehub.service.public_uploads;

import com.dalhousie.servicehub.util.UrlResourceHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequiredArgsConstructor
public class PublicUploadsServiceImpl implements PublicUploadsService {

    private final String uploadPath;
    private final UrlResourceHelper urlResourceHelper;

    @Override
    public Resource getFile(String filename) throws FileNotFoundException {
        Path filePath = Paths.get(uploadPath).resolve(filename).normalize();
        Resource resource = urlResourceHelper.getUrlResource(filePath.toUri());
        if (resource == null  || !resource.exists() || !resource.isReadable())
            throw new FileNotFoundException("File not found: " + filename);
        return resource;
    }
}
