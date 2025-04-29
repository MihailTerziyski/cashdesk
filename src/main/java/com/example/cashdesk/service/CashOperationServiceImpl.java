package com.example.cashdesk.service;

import com.example.cashdesk.model.api.request.CashOperationRequest;
import com.example.cashdesk.model.api.response.CashOperationResponse;
import com.example.cashdesk.model.api.response.DenominationResponse;
import com.example.cashdesk.model.service.CurrencyBalance;
import com.example.cashdesk.service.contract.BalanceFileService;
import com.example.cashdesk.service.contract.CashOperationService;
import com.example.cashdesk.service.contract.TransactionFileService;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import static com.example.cashdesk.model.enums.OperationType.DEPOSIT;

@Service
@RequiredArgsConstructor
public class CashOperationServiceImpl implements CashOperationService {

    private final CashierRegistryServiceImpl cashierRegistryServiceImpl;
    private final TransactionFileService transactionFileService;
    private final BalanceFileService balanceFileService;

    @Override
    public CashOperationResponse handleOperation(CashOperationRequest cashOperationRequest) {
        var cashier = cashierRegistryServiceImpl.getCashierByName(cashOperationRequest.getCashierName());

        if (cashier == null) {
            throw new IllegalArgumentException("Unknown cashier: " + cashOperationRequest.getCashierName());
        }

        var currencyEnum = cashOperationRequest.getCurrency();

        var currencyBalance = cashier.getBalancesByCurrency().get(currencyEnum);

        if(!isValidAmount(cashOperationRequest)) {
            throw new IllegalArgumentException("Invalid amount for the given denominations.");
        }

        if (DEPOSIT.equals(cashOperationRequest.getOperationType())) {
            applyDeposit(cashOperationRequest, currencyBalance);
            return buildResponse(cashOperationRequest, currencyBalance);
        }

        if (!hasSufficientCash(cashOperationRequest, currencyBalance)) {
            throw new IllegalArgumentException("Insufficient denominations for withdrawal.");
        }

        applyWithdrawal(cashOperationRequest, currencyBalance);

        return buildResponse(cashOperationRequest, currencyBalance);
    }

    private void applyDeposit(CashOperationRequest request, CurrencyBalance balance) {
        for (var denomination : request.getDenominations()) {
            balance.getDenominations().merge(denomination.getValue(), denomination.getCount(), Integer::sum);
        }

        balance.setTotalAmount(balance.getTotalAmount().add(request.getAmount()));

        transactionFileService.appendTransaction(request);
        balanceFileService.updateBalanceForCashier(request.getCashierName(), request.getCurrency());
    }

    private void applyWithdrawal(CashOperationRequest request, CurrencyBalance balance) {
        for (var denomination : request.getDenominations()) {
            var value = denomination.getValue();

            int currentCount = balance.getDenominations().getOrDefault(value, 0);
            balance.getDenominations().put(value, currentCount - denomination.getCount());
        }

        balance.setTotalAmount(balance.getTotalAmount().subtract(request.getAmount()));

        transactionFileService.appendTransaction(request);
        balanceFileService.updateBalanceForCashier(request.getCashierName(), request.getCurrency());
    }

    private boolean hasSufficientCash(CashOperationRequest request, CurrencyBalance balance) {
        return balance.getTotalAmount().compareTo(request.getAmount()) >= 0;
    }

    private CashOperationResponse buildResponse(CashOperationRequest cashOperationRequest,
            CurrencyBalance balance) {

        var responseDenominations = balance.getDenominations().entrySet().stream()
                .filter(e -> e.getValue() > 0)
                .map(e -> new DenominationResponse(e.getValue(), e.getKey()))
                .sorted(Comparator.comparing(DenominationResponse::getValue))
                .toList();

        var response = new CashOperationResponse();
        response.setCashierName(cashOperationRequest.getCashierName());
        response.setCurrency(cashOperationRequest.getCurrency());
        response.setAmount(balance.getTotalAmount());
        response.setDenominations(responseDenominations);
        response.setOperationType(cashOperationRequest.getOperationType());

        return response;
    }

    private boolean isValidAmount(CashOperationRequest request) {
        var calculatedTotal = request.getDenominations().stream()
                .map(denomination -> denomination.getValue().multiply(BigDecimal.valueOf(denomination.getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return calculatedTotal.compareTo(request.getAmount()) == 0;
    }
}
