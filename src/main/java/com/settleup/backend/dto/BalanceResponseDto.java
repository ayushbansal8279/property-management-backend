package com.settleup.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class BalanceResponseDto {

    private UUID partnerId;
    private String partnerName;
    private BigDecimal balance;
}