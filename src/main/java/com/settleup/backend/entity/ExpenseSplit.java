package com.settleup.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "expense_splits")
public class ExpenseSplit {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private Transaction transaction;

    @ManyToOne
    private Partner partner;

    private BigDecimal amount;
}