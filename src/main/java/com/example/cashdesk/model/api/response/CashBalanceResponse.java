package com.example.cashdesk.model.api.response;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CashBalanceResponse {

    private String cashierName;

    private String currency;

    private BigDecimal totalAmount;

    private List<DenominationResponse> denominations;
}
