package com.settleup.backend.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.settleup.backend.dto.BalanceResponseDto;
import com.settleup.backend.dto.SettlementResponseDto;
import com.settleup.backend.entity.ExpenseSplit;
import com.settleup.backend.entity.ExpenseSplitRepository;
import com.settleup.backend.entity.Partner;
import com.settleup.backend.entity.Transaction;
import com.settleup.backend.repository.PartnerRepository;
import com.settleup.backend.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final TransactionRepository transactionRepository;
    private final PartnerRepository partnerRepository;
    private final ExpenseSplitRepository splitRepository;;

    public List<BalanceResponseDto> calculateBalances(UUID ledgerId) {

        List<Transaction> transactions = transactionRepository.findByLedgerId(ledgerId);
        List<Partner> partners = partnerRepository.findByLedgerId(ledgerId);

        Map<UUID, BigDecimal> balanceMap = new HashMap<>();

        // ✅ Initialize all partners
        for (Partner p : partners) {
            balanceMap.put(p.getId(), BigDecimal.ZERO);
        }

        // =========================
        // 1️⃣ NORMAL TRANSACTIONS
        // =========================
        for (Transaction tx : transactions) {

            if ("EXPENSE".equals(tx.getType()))
                continue; // handled separately

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

        // =========================
        // 2️⃣ EXPENSE HANDLING 🔥
        // =========================
        for (Transaction tx : transactions) {

            if (!"EXPENSE".equals(tx.getType()))
                continue;

            UUID payerId = tx.getFromPartnerId();

            List<ExpenseSplit> splits = splitRepository.findByTransactionId(tx.getId());

            for (ExpenseSplit split : splits) {

                UUID partnerId = split.getPartner().getId();
                BigDecimal share = split.getAmount();

                // Each partner owes their share
                balanceMap.put(partnerId,
                        balanceMap.get(partnerId).subtract(share));

                // Payer should receive money
                balanceMap.put(payerId,
                        balanceMap.get(payerId).add(share));
            }
        }

        // =========================
        // 3️⃣ CONVERT TO DTO
        // =========================
        List<BalanceResponseDto> response = new ArrayList<>();

        for (Partner p : partners) {
            response.add(new BalanceResponseDto(
                    p.getId(),
                    p.getName(),
                    balanceMap.getOrDefault(p.getId(), BigDecimal.ZERO)));
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
                    settleAmount));

            // update balances
            debtor.setBalance(debtor.getBalance().add(settleAmount));
            creditor.setBalance(creditor.getBalance().subtract(settleAmount));

            // move pointers
            if (debtor.getBalance().compareTo(BigDecimal.ZERO) == 0)
                i++;
            if (creditor.getBalance().compareTo(BigDecimal.ZERO) == 0)
                j++;
        }

        return settlements;
    }
}