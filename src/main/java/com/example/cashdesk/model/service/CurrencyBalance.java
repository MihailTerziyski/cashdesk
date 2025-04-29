package com.example.cashdesk.model.service;

import java.math.BigDecimal;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyBalance {

    private BigDecimal totalAmount;

    private Map<BigDecimal, Integer> denominations;
}
