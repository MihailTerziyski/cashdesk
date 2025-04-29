package com.example.cashdesk.controller;

import com.example.cashdesk.model.api.response.CashBalanceResponse;
import com.example.cashdesk.service.contract.CashBalanceService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CashBalanceController {

    private final CashBalanceService cashBalanceService;

    @GetMapping("/cash-balance")
    public ResponseEntity<List<CashBalanceResponse>> getCashBalance(@RequestParam(required = false) String cashierName) {
        return ResponseEntity.ok(cashBalanceService.getCashBalancesWithFilters(cashierName));
    }
}
