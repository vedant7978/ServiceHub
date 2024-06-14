package com.dalhousie.servicehub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceDto {
    private Long id;
    private String description;
    private String name;
    private Double perHourRate;
    private String type;
    private Long providerId;
}
