package ru.otus.command;

import ru.otus.api.AtmCassette;
import ru.otus.api.Transaction;

import java.util.List;

public class BalanceCommand implements Transaction {
    private final List<AtmCassette> cassettesVault;

    public BalanceCommand(List<AtmCassette> cassettesVault) {
        this.cassettesVault = cassettesVault;
    }

    @Override
    public void execute() {
        int sum = cassettesVault.stream()
                .mapToInt(atmCassette -> atmCassette.denomination().value() * atmCassette.count())
                .sum();
        System.out.printf("BALANCE: %d RUB%n%s%n", sum, cassettesVault);
    }
}
