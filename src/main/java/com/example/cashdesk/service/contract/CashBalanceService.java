package com.example.cashdesk.service.contract;

import com.example.cashdesk.model.api.response.CashBalanceResponse;
import java.util.List;

public interface CashBalanceService {
    List<CashBalanceResponse> getCashBalancesWithFilters(String cashierName);
}
