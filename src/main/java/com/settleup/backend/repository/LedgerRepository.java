package com.settleup.backend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.settleup.backend.entity.Ledger;

public interface LedgerRepository extends JpaRepository<Ledger, UUID> {
}