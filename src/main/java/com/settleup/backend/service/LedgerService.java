package com.settleup.backend.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.settleup.backend.entity.Ledger;
import com.settleup.backend.entity.User;
import com.settleup.backend.repository.LedgerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LedgerService {

    private final LedgerRepository ledgerRepository;

    public Ledger createLedger(String name, User user) {
        Ledger ledger = new Ledger();
        ledger.setName(name);
        ledger.setCreatedBy(user);
        ledger.setCreatedAt(LocalDateTime.now());
        return ledgerRepository.save(ledger);
    }
}