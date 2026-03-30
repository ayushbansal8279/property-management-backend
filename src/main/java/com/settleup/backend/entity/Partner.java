package com.settleup.backend.entity;

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
@Table(name = "partners")
public class Partner {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private Ledger ledger;

    private String name;

    private String email;

    private String role;
}