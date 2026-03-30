package com.settleup.backend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PropertyRequestDto {

    private String name;
    private BigDecimal totalValue;
}