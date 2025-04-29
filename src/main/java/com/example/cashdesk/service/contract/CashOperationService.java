package com.example.cashdesk.service.contract;

import com.example.cashdesk.model.api.request.CashOperationRequest;
import com.example.cashdesk.model.api.response.CashOperationResponse;

public interface CashOperationService {
    CashOperationResponse handleOperation(CashOperationRequest cashOperationRequest);
}
