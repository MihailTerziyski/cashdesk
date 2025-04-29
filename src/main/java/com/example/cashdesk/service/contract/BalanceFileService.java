package com.example.cashdesk.service.contract;

import com.example.cashdesk.model.enums.CurrencyEnum;

public interface BalanceFileService {
    void updateBalanceForCashier(String cashierName, CurrencyEnum currency);
}
