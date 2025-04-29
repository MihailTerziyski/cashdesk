package com.example.cashdesk.service;

import com.example.cashdesk.exception.FileOperationException;
import com.example.cashdesk.model.api.response.CashBalanceResponse;
import com.example.cashdesk.model.api.response.DenominationResponse;
import com.example.cashdesk.model.enums.CurrencyEnum;
import com.example.cashdesk.model.service.Cashier;
import com.example.cashdesk.model.service.CurrencyBalance;
import com.example.cashdesk.service.contract.BalanceFileService;
import com.example.cashdesk.service.contract.CashierRegistryService;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BalanceFileServiceImpl implements BalanceFileService {

    private static final String BALANCES_FILE = "storage/balances.txt";

    private final CashierRegistryService cashierRegistryService;

    @Override
    public void updateBalanceForCashier(String cashierName, CurrencyEnum currency) {
        try {
            var path = Path.of(BALANCES_FILE);
            List<String> lines = new ArrayList<>();
            if (Files.exists(path)) {
                lines.addAll(Files.readAllLines(path));
            }

            var found = false;

            List<String> updatedLines = new ArrayList<>();

            for (String line : lines) {
                String[] parts = line.split(",");

                if (parts[0].equalsIgnoreCase(cashierName) && parts[1].equalsIgnoreCase(currency.name())) {
                    updatedLines.add(buildUpdatedLine(cashierName, currency));
                    found = true;
                } else {
                    updatedLines.add(line);
                }
            }

            if (!found) {
                updatedLines.add(buildUpdatedLine(cashierName, currency));
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(BALANCES_FILE, false))) {
                for (String updatedLine : updatedLines) {
                    writer.write(updatedLine);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            throw new FileOperationException("Failed to update balances file", e);
        }
    }

    public List<CashBalanceResponse> loadBalances() {
        try {
            Path path = Path.of(BALANCES_FILE);
            if (!Files.exists(path)) {
                return List.of();
            }

            var lines = Files.readAllLines(path);
            List<CashBalanceResponse> balances = new ArrayList<>();

            for (String line : lines) {
                balances.add(parseBalanceLine(line));
            }

            return balances;
        } catch (IOException e) {
            throw new FileOperationException("Failed to load balances from file", e);
        }
    }

    private CashBalanceResponse parseBalanceLine(String line) {
        var parts = line.split(",");
        if (parts.length < 3) {
            throw new FileOperationException("Invalid balance line: " + line);
        }

        var cashierName = parts[0];
        var currency = parts[1];
        var totalAmount = new BigDecimal(parts[2]);

        List<DenominationResponse> denominations = new ArrayList<>();
        for (int i = 3; i < parts.length; i++) {
            var denomParts = parts[i].split("x");
            int count = Integer.parseInt(denomParts[0]);
            var value = new BigDecimal(denomParts[1]);
            denominations.add(new DenominationResponse(count, value));
        }

        var response = new CashBalanceResponse();
        response.setCashierName(cashierName);
        response.setCurrency(currency);
        response.setTotalAmount(totalAmount);
        response.setDenominations(denominations);

        return response;
    }

    private String buildUpdatedLine(String cashierName, CurrencyEnum currency) {
        var cashier = cashierRegistryService.getCashierByName(cashierName);

        if (cashier == null) {
            throw new IllegalArgumentException("Unknown cashier: " + cashierName);
        }

        var balance = cashier.getBalancesByCurrency().get(currency);
        if (balance == null) {
            throw new IllegalArgumentException("Cashier has no balance in currency: " + currency);
        }

        var line = new StringBuilder();

        line.append(cashier.getName()).append(",")
                .append(currency.name()).append(",")
                .append(balance.getTotalAmount());

        for (var denominations : balance.getDenominations().entrySet()) {
            line.append(",")
                    .append(denominations.getValue())
                    .append("x")
                    .append(denominations.getKey());
        }

        return line.toString();
    }
}
