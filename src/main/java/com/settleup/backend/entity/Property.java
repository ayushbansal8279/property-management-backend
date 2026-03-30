package com.settleup.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

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