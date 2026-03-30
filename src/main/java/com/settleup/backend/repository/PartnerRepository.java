package com.settleup.backend.repository;

import com.settleup.backend.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PartnerRepository extends JpaRepository<Partner, UUID> {

    List<Partner> findByLedgerId(UUID ledgerId);
    List<Partner> findByUserId(UUID userId);
}