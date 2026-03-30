package com.settleup.backend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.settleup.backend.entity.Ledger;
import com.settleup.backend.entity.Partner;
import com.settleup.backend.entity.User;
import com.settleup.backend.repository.LedgerRepository;
import com.settleup.backend.repository.PartnerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PartnerService {

    private final PartnerRepository partnerRepository;
    private final LedgerRepository ledgerRepository;
    private final UserService userService;

    public Partner addPartner(UUID ledgerId, Partner partner) {

        Ledger ledger = ledgerRepository.findById(ledgerId)
                .orElseThrow(() -> new RuntimeException("Ledger not found"));

        partner.setLedger(ledger);

        return partnerRepository.save(partner);
    }

    public List<Partner> getPartners(UUID ledgerId) {
        return partnerRepository.findByLedgerId(ledgerId);
    }

    public void deletePartner(UUID partnerId) {
        partnerRepository.deleteById(partnerId);
    }

    public Partner addPartner(UUID ledgerId, String name, String email, String role) {

        Ledger ledger = ledgerRepository.findById(ledgerId)
                .orElseThrow(() -> new RuntimeException("Ledger not found"));

        Partner partner = new Partner();
        partner.setLedger(ledger);
        partner.setName(name);
        partner.setEmail(email);
        partner.setRole(role);

        // 🔥 only create user if email exists
        if (email != null && !email.isBlank()) {
            User user = userService.getOrCreateUser(name, email);
            partner.setUser(user);
        }

        return partnerRepository.save(partner);
    }

    public Partner updatePartnerEmail(UUID partnerId, String email) {

        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Partner not found"));

        partner.setEmail(email);

        User user = userService.getOrCreateUser(partner.getName(), email);
        partner.setUser(user);

        return partnerRepository.save(partner);
    }
}