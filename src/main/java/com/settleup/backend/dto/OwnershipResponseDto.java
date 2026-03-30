package com.settleup.backend.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OwnershipResponseDto {

    private String partnerName;
    private BigDecimal investmentAmount;
    private BigDecimal ownershipPercentage;
}