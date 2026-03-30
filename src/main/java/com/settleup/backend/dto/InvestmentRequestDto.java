package com.settleup.backend.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter

public class InvestmentRequestDto {

    private UUID partnerId; // ✅ who is investing
    private BigDecimal amount; // ✅ amount invested
}