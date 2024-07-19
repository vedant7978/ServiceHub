package com.dalhousie.servicehub.util;

import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URI;

@Component
public class UrlResourceHelper {

    /**
     * Provides the UrlResource for the requesting URI
     * @param uri URI instance to get UrlResource
     * @return UrlResource instance
     */
    public UrlResource getUrlResource(URI uri) {
        try {
            return new UrlResource(uri);
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
