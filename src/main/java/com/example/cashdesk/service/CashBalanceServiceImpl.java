package com.example.cashdesk.service;

import com.example.cashdesk.model.api.response.CashBalanceResponse;
import com.example.cashdesk.service.contract.CashBalanceService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class CashBalanceServiceImpl implements CashBalanceService {

    private final BalanceFileServiceImpl balanceFileService;

    @Override
    public List<CashBalanceResponse> getCashBalancesWithFilters(String cashierName) {
        var balances = balanceFileService.loadBalances();

        return balances.stream()
                .filter(balance -> cashierName == null || balance.getCashierName().equalsIgnoreCase(cashierName))
                .toList();
    }
}
