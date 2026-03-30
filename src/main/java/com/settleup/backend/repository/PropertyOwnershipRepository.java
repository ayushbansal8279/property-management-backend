package com.settleup.backend.repository;

import com.settleup.backend.entity.PropertyOwnership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PropertyOwnershipRepository extends JpaRepository<PropertyOwnership, UUID> {

    List<PropertyOwnership> findByPropertyId(UUID propertyId);
}