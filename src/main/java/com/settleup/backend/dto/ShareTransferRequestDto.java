package com.settleup.backend.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ShareTransferRequestDto {

    private UUID fromPartnerId;
    private UUID toPartnerId;
    private BigDecimal percentage;
    private BigDecimal amountPaid;
    private String comments;
}