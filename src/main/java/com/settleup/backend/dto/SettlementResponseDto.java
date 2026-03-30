package com.settleup.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class SettlementResponseDto {

    private UUID fromPartnerId;
    private String fromPartnerName;

    private UUID toPartnerId;
    private String toPartnerName;

    private BigDecimal amount;
}