package com.example.cashdesk.model.api.request;

import com.example.cashdesk.model.enums.CurrencyEnum;
import com.example.cashdesk.model.enums.OperationType;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CashOperationRequest {

    private OperationType operationType;

    private CurrencyEnum currency;

    private BigDecimal amount;

    private String cashierName;

    private List<DenominationRequest> denominations;
}
