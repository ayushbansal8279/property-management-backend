package com.settleup.backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.settleup.backend.dto.OwnershipResponseDto;
import com.settleup.backend.entity.Partner;
import com.settleup.backend.entity.Property;
import com.settleup.backend.entity.PropertyOwnership;
import com.settleup.backend.entity.Transaction;
import com.settleup.backend.repository.PartnerRepository;
import com.settleup.backend.repository.PropertyOwnershipRepository;
import com.settleup.backend.repository.PropertyRepository;
import com.settleup.backend.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PropertyOwnershipService {

        private final PropertyOwnershipRepository ownershipRepository;
        private final PropertyRepository propertyRepository;
        private final PartnerRepository partnerRepository;
        private final TransactionRepository transactionRepository;

        // 💰 INVESTMENT (does NOT change %)
        public void addInvestment(UUID propertyId, UUID partnerId, BigDecimal amount) {

                Property property = propertyRepository.findById(propertyId)
                                .orElseThrow(() -> new RuntimeException("Property not found"));

                Partner partner = partnerRepository.findById(partnerId)
                                .orElseThrow(() -> new RuntimeException("Partner not found"));

                PropertyOwnership ownership = ownershipRepository.findByPropertyId(propertyId)
                                .stream()
                                .filter(o -> o.getPartner().getId().equals(partnerId))
                                .findFirst()
                                .orElseGet(() -> {
                                        PropertyOwnership o = new PropertyOwnership();
                                        o.setProperty(property);
                                        o.setPartner(partner);
                                        o.setOwnershipPercentage(BigDecimal.ZERO); // initially 0%
                                        o.setInvestmentAmount(BigDecimal.ZERO);
                                        return o;
                                });

                ownership.setInvestmentAmount(
                                ownership.getInvestmentAmount().add(amount));

                ownershipRepository.save(ownership);
        }

        // 🔄 SHARE TRANSFER (REAL LOGIC)
        public void transferShare(UUID propertyId,
                        UUID fromId,
                        UUID toId,
                        BigDecimal percentage,
                        BigDecimal amountPaid) {

                Property property = propertyRepository.findById(propertyId)
                                .orElseThrow();

                List<PropertyOwnership> list = ownershipRepository.findByPropertyId(propertyId);

                PropertyOwnership from = list.stream()
                                .filter(o -> o.getPartner().getId().equals(fromId))
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException("From partner not found"));

                PropertyOwnership to = list.stream()
                                .filter(o -> o.getPartner().getId().equals(toId))
                                .findFirst()
                                .orElseGet(() -> {
                                        PropertyOwnership o = new PropertyOwnership();
                                        o.setProperty(property);
                                        o.setPartner(partnerRepository.findById(toId).orElseThrow());
                                        o.setOwnershipPercentage(BigDecimal.ZERO);
                                        o.setInvestmentAmount(BigDecimal.ZERO);
                                        return o;
                                });

                // 🔥 VALIDATION
                if (from.getOwnershipPercentage().compareTo(percentage) < 0) {
                        throw new RuntimeException("Not enough ownership to transfer");
                }

                // 🔥 STEP 1: Update ownership %
                from.setOwnershipPercentage(from.getOwnershipPercentage().subtract(percentage));
                to.setOwnershipPercentage(to.getOwnershipPercentage().add(percentage));

                // 🔥 STEP 2: Calculate BOOK VALUE
                BigDecimal bookValue = property.getTotalValue()
                                .multiply(percentage)
                                .divide(BigDecimal.valueOf(100));

                // 🔥 STEP 3: Update investment
                from.setInvestmentAmount(from.getInvestmentAmount().subtract(bookValue));
                to.setInvestmentAmount(to.getInvestmentAmount().add(amountPaid));

                ownershipRepository.save(from);
                ownershipRepository.save(to);

                // 🔥 STEP 4: Record transaction
                Transaction tx = new Transaction();
                tx.setType("SHARE_TRANSFER");
                tx.setFromPartnerId(toId); // buyer pays
                tx.setToPartnerId(fromId); // seller receives
                tx.setAmount(amountPaid);
                tx.setProperty(property);
                tx.setLedger(property.getLedger());
                tx.setDescription("Share transfer");
                tx.setCreatedAt(LocalDateTime.now());

                transactionRepository.save(tx);
        }

        // 📋 GET OWNERSHIP
        public List<OwnershipResponseDto> getOwnership(UUID propertyId) {

                return ownershipRepository.findByPropertyId(propertyId)
                                .stream()
                                .map(o -> new OwnershipResponseDto(
                                                o.getPartner().getName(),
                                                o.getInvestmentAmount(),
                                                o.getOwnershipPercentage()))
                                .toList();
        }
}
