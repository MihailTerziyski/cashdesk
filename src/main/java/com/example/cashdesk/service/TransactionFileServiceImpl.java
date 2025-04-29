package com.example.cashdesk.service;

import com.example.cashdesk.model.api.request.CashOperationRequest;
import com.example.cashdesk.model.api.request.DenominationRequest;
import com.example.cashdesk.service.contract.TransactionFileService;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TransactionFileServiceImpl implements TransactionFileService {

    private static final String TRANSACTIONS_FILE = "storage/transactions.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;


    @Override
    public void appendTransaction(CashOperationRequest request) {
        String line = buildTransactionLine(request);
        appendLineToFile(line);
    }


    private String buildTransactionLine(CashOperationRequest request) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String cashier = request.getCashierName();
        String operation = request.getOperationType().toString();
        String currency = request.getCurrency().toString();
        BigDecimal amount = request.getAmount();
        String denominations = buildDenominationsString(request.getDenominations());

        return String.join(",", timestamp, cashier, operation, currency, amount.toPlainString(), denominations);
    }

    private String buildDenominationsString(List<DenominationRequest> denominations) {
        return denominations.stream()
                .map(d -> d.getCount() + "x" + d.getValue())
                .reduce((a, b) -> a + "," + b)
                .orElse("");
    }

    private void appendLineToFile(String line) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTIONS_FILE, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Failed to write transaction to file", e);
        }
    }

}
