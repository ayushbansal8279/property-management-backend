package com.settleup.backend.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.settleup.backend.entity.Partner;

public interface PartnerRepository extends JpaRepository<Partner, UUID> {

    List<Partner> findByLedgerId(UUID ledgerId);
}