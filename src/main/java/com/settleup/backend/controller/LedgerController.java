package com.settleup.backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.settleup.backend.entity.Ledger;
import com.settleup.backend.entity.User;
import com.settleup.backend.repository.UserRepository;
import com.settleup.backend.service.LedgerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ledgers")
@RequiredArgsConstructor
public class LedgerController {

    private final LedgerService ledgerService;
    private final UserRepository userRepository;

    @PostMapping
    public Ledger create(@RequestParam String name) {

        // ✅ Fetch existing user (MUST exist in DB)
        User user = userRepository.findByEmail("ayush@test.com")
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ledgerService.createLedger(name, user);
    }
}