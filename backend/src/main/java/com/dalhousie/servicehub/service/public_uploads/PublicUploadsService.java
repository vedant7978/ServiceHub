package com.dalhousie.servicehub.service.public_uploads;

import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;

public interface PublicUploadsService {

    /**
     * Provides the file with the requesting file name
     * @param filename Name of the file to get
     * @return Resource representing the file
     */
    Resource getFile(String filename) throws FileNotFoundException;
}
