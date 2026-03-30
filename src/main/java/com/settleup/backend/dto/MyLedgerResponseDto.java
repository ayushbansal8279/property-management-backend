package com.settleup.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class MyLedgerResponseDto {

    private UUID ledgerId;
    private String ledgerName;
    private String role;
}