package com.dalhousie.servicehub.util;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class FileHelper {

    /**
     * Writes bytes to the specific path
     * @param path Path of the file to write
     * @param bytes Bytes array to write to file
     */
    public void write(Path path, byte[] bytes) {
        try {
            Files.write(path, bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
