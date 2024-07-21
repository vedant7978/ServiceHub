package com.dalhousie.servicehub.dto;

import com.dalhousie.servicehub.enums.ContractStatus;
import com.dalhousie.servicehub.enums.HistoryType;
import com.dalhousie.servicehub.enums.ServiceType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class HistoryContractDto {
    private Long id;
    private String serviceName;
    private ServiceType serviceType;
    private HistoryType historyType;
    private String serviceProviderName;
    private Double perHourRate;
    private String serviceDescription;
    private String userImageUrl;
    private String userName;
    private ContractStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
