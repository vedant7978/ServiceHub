package com.dalhousie.servicehub.service;

import com.dalhousie.servicehub.service.public_uploads.PublicUploadsServiceImpl;
import com.dalhousie.servicehub.util.UrlResourceHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PublicUploadsServiceTest {

    private static final Logger logger = LogManager.getLogger(PublicUploadsServiceTest.class);

    @Mock
    private UrlResourceHelper urlResourceHelper;

    private PublicUploadsServiceImpl publicUploadsService;

    @BeforeEach
    public void initialSetup() {
        publicUploadsService = new PublicUploadsServiceImpl("backend/uploads", urlResourceHelper);
    }

    @Test
    public void shouldThrowException_whenFileIsNull() {
        // Given
        logger.info("Test started: Reading file when it is null");
        AtomicReference<Resource> resource = new AtomicReference<>();
        String fileName = "abcd.png";

        // When
        FileNotFoundException fileNotFoundException = assertThrows(FileNotFoundException.class,
                () -> resource.set(publicUploadsService.getFile(fileName)));

        // Then
        assertNull(resource.get());
        assertEquals("File not found: " + fileName, fileNotFoundException.getMessage());
        logger.info("Test finished: Reading file when it is null");
    }

    @Test
    public void shouldThrowException_whenFileIsNotPresent() {
        // Given
        logger.info("Test started: Reading file when it is not present");
        AtomicReference<Resource> resource = new AtomicReference<>();
        String fileName = "abcd.png";

        logger.info("UrlResource with name {} does not exist", fileName);
        UrlResource mockResource = mock(UrlResource.class);
        when(mockResource.exists()).thenReturn(false);
        when(urlResourceHelper.getUrlResource(any())).thenReturn(mockResource);

        // When
        FileNotFoundException fileNotFoundException = assertThrows(FileNotFoundException.class,
                () -> resource.set(publicUploadsService.getFile(fileName)));

        // Then
        assertNull(resource.get());
        assertEquals("File not found: " + fileName, fileNotFoundException.getMessage());
        logger.info("Test finished: Reading file when it is not present");
    }

    @Test
    public void shouldThrowException_whenFileIsNotReadable() {
        // Given
        logger.info("Test started: Reading file when it is not readable");
        AtomicReference<Resource> resource = new AtomicReference<>();
        String fileName = "abcd.png";

        logger.info("UrlResource with name {} it is not readable", fileName);
        UrlResource mockResource = mock(UrlResource.class);
        when(mockResource.exists()).thenReturn(true);
        when(mockResource.isReadable()).thenReturn(false);
        when(urlResourceHelper.getUrlResource(any())).thenReturn(mockResource);

        // When
        FileNotFoundException fileNotFoundException = assertThrows(FileNotFoundException.class,
                () -> resource.set(publicUploadsService.getFile(fileName)));

        // Then
        assertNull(resource.get());
        assertEquals("File not found: " + fileName, fileNotFoundException.getMessage());
        logger.info("Test finished: Reading file when it is not readable");
    }

    @Test
    public void shouldReturnResource_whenFileIsPresentAndReadable() {
        // Given
        logger.info("Test started: Reading file when it is present and readable");
        AtomicReference<Resource> resource = new AtomicReference<>();
        String fileName = "abcd.png";

        logger.info("UrlResource with name {} will exists and will be readable", fileName);
        UrlResource mockResource = mock(UrlResource.class);
        when(mockResource.exists()).thenReturn(true);
        when(mockResource.isReadable()).thenReturn(true);
        when(mockResource.getFilename()).thenReturn(fileName);
        when(urlResourceHelper.getUrlResource(any())).thenReturn(mockResource);

        // When
        assertDoesNotThrow(() -> resource.set(publicUploadsService.getFile(fileName)));

        // Then
        assertNotNull(resource.get());
        assertEquals(fileName, resource.get().getFilename());
        logger.info("Test finished: Reading file when it is present and readable");
    }
}
