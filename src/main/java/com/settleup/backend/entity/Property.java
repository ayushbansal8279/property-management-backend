package com.settleup.backend.entity;

import java.math.BigDecimal;
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
@Table(name = "properties")
public class Property {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private Ledger ledger;

    private String name;

    private BigDecimal totalValue;
}