package com.settleup.backend.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.settleup.backend.dto.PropertyRequestDto;
import com.settleup.backend.dto.PropertyResponseDto;
import com.settleup.backend.entity.Ledger;
import com.settleup.backend.entity.Property;
import com.settleup.backend.repository.LedgerRepository;
import com.settleup.backend.repository.PropertyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PropertyService {

        private final PropertyRepository propertyRepository;
        private final LedgerRepository ledgerRepository;

        public PropertyResponseDto createProperty(UUID ledgerId, PropertyRequestDto request) {

                Ledger ledger = ledgerRepository.findById(ledgerId)
                                .orElseThrow(() -> new RuntimeException("Ledger not found"));

                Property property = new Property();
                property.setLedger(ledger);
                property.setName(request.getName());
                property.setTotalValue(request.getTotalValue());

                Property saved = propertyRepository.save(property);

                return new PropertyResponseDto(
                                saved.getId(),
                                saved.getName(),
                                saved.getTotalValue());
        }

        public List<PropertyResponseDto> getProperties(UUID ledgerId) {

                return propertyRepository.findByLedgerId(ledgerId)
                                .stream()
                                .map(p -> new PropertyResponseDto(
                                                p.getId(),
                                                p.getName(),
                                                p.getTotalValue()))
                                .collect(Collectors.toList());
        }
}