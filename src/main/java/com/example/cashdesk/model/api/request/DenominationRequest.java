package com.example.cashdesk.model.api.request;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DenominationRequest {
    private int count;

    private BigDecimal value;
}
