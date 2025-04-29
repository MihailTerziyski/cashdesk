package com.example.cashdesk.service.contract;

import com.example.cashdesk.model.service.Cashier;

public interface CashierRegistryService {
    Cashier getCashierByName(String name);
}
