package com.settleup.backend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.settleup.backend.dto.InvestmentRequestDto;
import com.settleup.backend.dto.OwnershipResponseDto;
import com.settleup.backend.dto.ShareTransferRequestDto;
import com.settleup.backend.service.PropertyOwnershipService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ownership")
@RequiredArgsConstructor
public class PropertyOwnershipController {

    private final PropertyOwnershipService service;

    // 💰 INVEST
    @PostMapping("/invest/{propertyId}")
    public void invest(@PathVariable UUID propertyId,
            @RequestBody InvestmentRequestDto req) {

        service.addInvestment(propertyId, req.getPartnerId(), req.getAmount());
    }

    // 🔄 TRANSFER
    @PostMapping("/transfer/{propertyId}")
    public void transfer(@PathVariable UUID propertyId,
            @RequestBody ShareTransferRequestDto req) {

        service.transferShare(
                propertyId,
                req.getFromPartnerId(),
                req.getToPartnerId(),
                req.getPercentage(),
                req.getAmountPaid());
    }

    // 📋 GET
    @GetMapping("/{propertyId}")
    public List<OwnershipResponseDto> get(@PathVariable UUID propertyId) {
        return service.getOwnership(propertyId);
    }
}