package com.settleup.backend.service;

import com.settleup.backend.dto.BalanceResponseDto;
import com.settleup.backend.entity.Partner;
import com.settleup.backend.entity.Transaction;
import com.settleup.backend.repository.PartnerRepository;
import com.settleup.backend.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final TransactionRepository transactionRepository;
    private final PartnerRepository partnerRepository;

    public List<BalanceResponseDto> calculateBalances(UUID ledgerId) {

        List<Transaction> transactions = transactionRepository.findByLedgerId(ledgerId);
        List<Partner> partners = partnerRepository.findByLedgerId(ledgerId);

        Map<UUID, BigDecimal> balanceMap = new HashMap<>();

        // initialize all partners with 0
        for (Partner partner : partners) {
            balanceMap.put(partner.getId(), BigDecimal.ZERO);
        }

        // calculate balances
        for (Transaction tx : transactions) {

            UUID from = tx.getFromPartnerId();
            UUID to = tx.getToPartnerId();
            BigDecimal amount = tx.getAmount();

            if (from != null) {
                balanceMap.put(from, balanceMap.get(from).subtract(amount));
            }

            if (to != null) {
                balanceMap.put(to, balanceMap.get(to).add(amount));
            }
        }

        // convert to DTO
        List<BalanceResponseDto> response = new ArrayList<>();

        for (Partner partner : partners) {

            BigDecimal balance = balanceMap.getOrDefault(partner.getId(), BigDecimal.ZERO);

            response.add(new BalanceResponseDto(
                    partner.getId(),
                    partner.getName(),
                    balance
            ));
        }

        return response;
    }

    public List<SettlementResponseDto> calculateSettlements(UUID ledgerId) {

    List<BalanceResponseDto> balances = calculateBalances(ledgerId);

    List<BalanceResponseDto> creditors = new ArrayList<>();
    List<BalanceResponseDto> debtors = new ArrayList<>();

    // separate creditors & debtors
    for (BalanceResponseDto b : balances) {
        if (b.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            creditors.add(b);
        } else if (b.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            debtors.add(b);
        }
    }

    List<SettlementResponseDto> settlements = new ArrayList<>();

    int i = 0, j = 0;

    while (i < debtors.size() && j < creditors.size()) {

        BalanceResponseDto debtor = debtors.get(i);
        BalanceResponseDto creditor = creditors.get(j);

        BigDecimal debtAmount = debtor.getBalance().abs();
        BigDecimal creditAmount = creditor.getBalance();

        BigDecimal settleAmount = debtAmount.min(creditAmount);

        settlements.add(new SettlementResponseDto(
                debtor.getPartnerId(),
                debtor.getPartnerName(),
                creditor.getPartnerId(),
                creditor.getPartnerName(),
                settleAmount
        ));

        // update balances
        debtor.setBalance(debtor.getBalance().add(settleAmount));
        creditor.setBalance(creditor.getBalance().subtract(settleAmount));

        // move pointers
        if (debtor.getBalance().compareTo(BigDecimal.ZERO) == 0) i++;
        if (creditor.getBalance().compareTo(BigDecimal.ZERO) == 0) j++;
    }

    return settlements;
}
}