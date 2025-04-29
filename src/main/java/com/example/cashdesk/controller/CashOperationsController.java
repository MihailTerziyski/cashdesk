package com.example.cashdesk.controller;

import com.example.cashdesk.model.api.request.CashOperationRequest;
import com.example.cashdesk.model.api.response.CashOperationResponse;
import com.example.cashdesk.service.contract.CashOperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CashOperationsController {
    private final CashOperationService cashOperationService;

    @PostMapping("/cash-operation")
    public ResponseEntity<CashOperationResponse> performOperation(@RequestBody CashOperationRequest cashOperationRequest) {
        var response = cashOperationService.handleOperation(cashOperationRequest);

        return ResponseEntity.ok(response);
    }
}
