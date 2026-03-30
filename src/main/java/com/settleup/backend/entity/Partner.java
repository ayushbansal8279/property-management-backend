package com.settleup.backend.entity;

import java.util.UUID;

import jakarta.persistence.Column;
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

    // 🔥 optional user
    @ManyToOne
    private User user;   // can be null

    private String name;

    @Column(nullable = true)
    private String email;  // optional

    private String role;
}