package com.example.cashdesk.model.service;

import com.example.cashdesk.model.api.request.DenominationRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRecord {
    private LocalDateTime timestamp;
    private String cashierName;
    private String operationType; // DEPOSIT or WITHDRAWAL
    private String currency;
    private BigDecimal amount;
    private List<DenominationRequest> denominations;
}

