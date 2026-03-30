package com.settleup.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "property_ownerships")
public class PropertyOwnership {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private Property property;

    @ManyToOne
    private Partner partner;

    private BigDecimal investmentAmount;   // ₹ invested

    private BigDecimal ownershipPercentage; // %
}