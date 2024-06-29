package com.dalhousie.servicehub.response;

import com.dalhousie.servicehub.dto.UserDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetProviderResponse {
    private UserDto provider;
}
