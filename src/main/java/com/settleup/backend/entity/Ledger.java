package com.settleup.backend.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "ledgers")
public class Ledger {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @ManyToOne
    private User createdBy;

    private LocalDateTime createdAt;
}