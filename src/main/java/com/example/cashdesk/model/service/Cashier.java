package com.example.cashdesk.model.service;

import com.example.cashdesk.model.enums.CurrencyEnum;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cashier {
    private String name;

    private Map<CurrencyEnum, CurrencyBalance> balancesByCurrency;
}
