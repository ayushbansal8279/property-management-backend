package com.settleup.backend.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.settleup.backend.entity.Partner;
import com.settleup.backend.service.PartnerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/partners")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;

    // ➕ Add partner
    @PostMapping("/{ledgerId}")
    public Partner addPartner(@PathVariable UUID ledgerId,
            @RequestBody Map<String, String> request) {

        return partnerService.addPartner(
                ledgerId,
                request.get("name"),
                request.get("email"),
                request.get("role"));
    }

    // 📋 Get partners of ledger
    @GetMapping("/{ledgerId}")
    public List<Partner> getPartners(@PathVariable UUID ledgerId) {
        return partnerService.getPartners(ledgerId);
    }

    // ❌ Delete partner
    @DeleteMapping("/{partnerId}")
    public void delete(@PathVariable UUID partnerId) {
        partnerService.deletePartner(partnerId);
    }
}