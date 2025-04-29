package com.example.cashdesk.model.api.response;

import com.example.cashdesk.model.api.request.DenominationRequest;
import com.example.cashdesk.model.enums.CurrencyEnum;
import com.example.cashdesk.model.enums.OperationType;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CashOperationResponse {
    private OperationType operationType;

    private String cashierName;

    private CurrencyEnum currency;

    private BigDecimal amount;

    private List<DenominationResponse> denominations;
}
