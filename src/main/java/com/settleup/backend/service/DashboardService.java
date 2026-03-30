package com.settleup.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.settleup.backend.dto.MyLedgerResponseDto;
import com.settleup.backend.entity.Partner;
import com.settleup.backend.entity.User;
import com.settleup.backend.repository.PartnerRepository;
import com.settleup.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final PartnerRepository partnerRepository;
    private final UserRepository userRepository;

    public List<MyLedgerResponseDto> getMyLedgers(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Partner> partners = partnerRepository.findByUserId(user.getId());

        List<MyLedgerResponseDto> response = new ArrayList<>();

        for (Partner p : partners) {
            response.add(new MyLedgerResponseDto(
                    p.getLedger().getId(),
                    p.getLedger().getName(),
                    p.getRole()));
        }

        return response;
    }
}