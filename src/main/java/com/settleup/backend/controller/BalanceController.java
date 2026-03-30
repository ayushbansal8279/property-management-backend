package com.settleup.backend.controller;

import com.settleup.backend.dto.BalanceResponseDto;
import com.settleup.backend.service.BalanceService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/balances")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping("/{ledgerId}")
    public List<BalanceResponseDto> getBalances(@PathVariable UUID ledgerId) {
        return balanceService.calculateBalances(ledgerId);
    }

    @GetMapping("/settlements/{ledgerId}")
public List<SettlementResponseDto> getSettlements(@PathVariable UUID ledgerId) {
    return balanceService.calculateSettlements(ledgerId);
}
@PostMapping("/expense")
public void addExpense(@RequestBody ExpenseRequestDto req) {
    expenseService.addExpense(req);
}
}