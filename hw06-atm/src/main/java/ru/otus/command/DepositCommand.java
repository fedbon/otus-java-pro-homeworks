package ru.otus.command;

import ru.otus.api.AtmCassette;
import ru.otus.api.Bill;
import ru.otus.api.Transaction;
import ru.otus.model.Denomination;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class DepositCommand implements Transaction {
    private final Map<Denomination, List<Bill>> billsMap;
    private final Map<Denomination, AtmCassette> cassettesVaultMap;

    public DepositCommand(List<Bill> billsMap, List<AtmCassette> cassettesVault) {
        this.billsMap = billsMap.stream()
                .collect(Collectors.groupingBy(Bill::denomination, toList()));
        this.cassettesVaultMap = cassettesVault.stream()
                .collect(Collectors.toMap(AtmCassette::denomination, Function.identity()));
    }

    @Override
    public void execute() {
        for (Map.Entry<Denomination, List<Bill>> entry : billsMap.entrySet()) {
            var denomination = entry.getKey();
            List<Bill> introducedBills = this.billsMap.get(denomination);

            var atmCassette = cassettesVaultMap.get(denomination);
            if (atmCassette != null) {
                atmCassette.put(introducedBills);
            }
        }
        System.out.printf("DEPOSITED BILLS: %n%s%n", billsMap.values());
    }
}
