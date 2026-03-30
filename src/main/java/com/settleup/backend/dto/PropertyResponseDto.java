package com.settleup.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class PropertyResponseDto {

    private UUID id;
    private String name;
    private BigDecimal totalValue;
}