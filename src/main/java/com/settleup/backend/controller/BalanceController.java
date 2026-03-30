package com.settleup.backend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.settleup.backend.dto.BalanceResponseDto;
import com.settleup.backend.dto.ExpenseRequestDto;
import com.settleup.backend.dto.SettlementResponseDto;
import com.settleup.backend.service.BalanceService;
import com.settleup.backend.service.ExpenseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/balances")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;
    private final ExpenseService expenseService;

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