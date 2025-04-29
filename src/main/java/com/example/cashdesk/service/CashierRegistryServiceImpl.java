package com.example.cashdesk.service;

import com.example.cashdesk.model.enums.CurrencyEnum;
import com.example.cashdesk.model.service.Cashier;
import com.example.cashdesk.model.service.CurrencyBalance;
import com.example.cashdesk.service.contract.CashierRegistryService;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class CashierRegistryServiceImpl implements CashierRegistryService {

    private final Map<String, Cashier> cashiersByName = new ConcurrentHashMap<>();

    public CashierRegistryServiceImpl() {
        initializeCashiers();
    }

    @Override
    public Cashier getCashierByName(String name) {
        return cashiersByName.get(name);
    }

    private void initializeCashiers() {
        cashiersByName.put("MARTINA", createInitialCashier("MARTINA"));
        cashiersByName.put("PETER", createInitialCashier("PETER"));
        cashiersByName.put("LINDA", createInitialCashier("LINDA"));
    }

    private Cashier createInitialCashier(String name) {
        Cashier cashier = new Cashier();
        cashier.setName(name);

        Map<CurrencyEnum, CurrencyBalance> balances = new ConcurrentHashMap<>();

        CurrencyBalance bgn = new CurrencyBalance();
        bgn.setTotalAmount(BigDecimal.valueOf(1000));
        bgn.setDenominations(new HashMap<>(Map.of(
                BigDecimal.valueOf(10), 50,
                BigDecimal.valueOf(50), 10
        )));

        CurrencyBalance eur = new CurrencyBalance();
        eur.setTotalAmount(BigDecimal.valueOf(2000));
        eur.setDenominations(new HashMap<>(Map.of(
                BigDecimal.valueOf(10), 100,
                BigDecimal.valueOf(50), 20
        )));

        balances.put(CurrencyEnum.BGN, bgn);
        balances.put(CurrencyEnum.EUR, eur);

        cashier.setBalancesByCurrency(balances);
        return cashier;
    }
}
