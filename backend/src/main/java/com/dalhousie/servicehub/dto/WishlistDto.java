package com.dalhousie.servicehub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishlistDto {

    private Long id;
    private Long serviceId;
    private Long userId;
}
