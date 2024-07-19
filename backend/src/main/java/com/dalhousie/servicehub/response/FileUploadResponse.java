package com.dalhousie.servicehub.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileUploadResponse {
    private String url;
}
