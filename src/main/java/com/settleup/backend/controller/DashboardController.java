package com.settleup.backend.controller;

import com.settleup.backend.dto.MyLedgerResponseDto;
import com.settleup.backend.service.DashboardService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    // 🔥 TEMP: pass email manually (JWT later)
    @GetMapping("/my-ledgers")
    public List<MyLedgerResponseDto> getMyLedgers(@RequestParam String email) {
        return dashboardService.getMyLedgers(email);
    }
}