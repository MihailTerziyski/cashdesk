package com.example.cashdesk.service.contract;

import com.example.cashdesk.model.api.request.CashOperationRequest;
import com.example.cashdesk.model.service.TransactionRecord;
import java.util.List;

public interface TransactionFileService {
    void appendTransaction(CashOperationRequest request);
}
