package ru.otus.command;

import ru.otus.api.Transaction;
import ru.otus.exception.NotEnoughMoneyException;
import ru.otus.api.AtmCassette;
import ru.otus.api.Bill;
import ru.otus.model.Denomination;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class WithdrawCommand implements Transaction {
    private final int amount;
    private final List<AtmCassette> cassettesVault;

    public WithdrawCommand(int amount, List<AtmCassette> cassettesVault) {
        this.amount = amount;
        this.cassettesVault = cassettesVault;
    }

    @Override
    public void execute() {
        cassettesVault.sort(Comparator.comparing(AtmCassette::denomination,
                Comparator.comparingInt(Denomination::value)).reversed());

        int sum = amount;

        List<Bill> billsForWithdraw = new ArrayList<>();
        for (var atmCassette : cassettesVault) {
            var denomination = atmCassette.denomination();

            while (sum - denomination.value() >= 0 && atmCassette.count() > 0) {
                var bill = atmCassette.take();
                sum = sum - denomination.value();

                billsForWithdraw.add(bill);
            }
        }
        if (sum != 0) {
            returnBillsToAtmCassettes(billsForWithdraw);
            throw new NotEnoughMoneyException("TEMPORARILY UNABLE TO DISPENSE CASH!");
        } else {
            System.out.println("WITHDRAWN AMOUNT: " + amount + " " + billsForWithdraw);
        }
    }

    private void returnBillsToAtmCassettes(List<Bill> resultCash) {
        resultCash.stream()
            .collect(Collectors.groupingBy(Bill::denomination, Collectors.toList()))
                .forEach((denomination, bills) -> {
                    for (var atmCassette : cassettesVault) {
                        if (atmCassette.denomination() == denomination) {
                            atmCassette.put(bills);
                        }
                    }
                });
    }
}
