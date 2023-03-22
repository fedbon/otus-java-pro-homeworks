package ru.otus.model;

import ru.otus.api.AtmCassette;
import ru.otus.api.AtmController;
import ru.otus.api.Bill;
import ru.otus.command.BalanceCommand;
import ru.otus.command.DepositCommand;
import ru.otus.command.WithdrawCommand;
import ru.otus.exception.NotEnoughMoneyException;

import java.util.List;

public class AtmControllerImpl implements AtmController {
    private final List<AtmCassette> cassettesVault;

    public AtmControllerImpl(List<AtmCassette> cassettesVault) {
        this.cassettesVault = cassettesVault;
    }

    @Override
    public void withdraw(int amount) {
        var withdrawCommand = new WithdrawCommand(amount, cassettesVault);
        try {
            withdrawCommand.execute();
        } catch (NotEnoughMoneyException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deposit(List<Bill> bills) {
        var depositCommand = new DepositCommand(bills, cassettesVault);
        depositCommand.execute();
    }

    @Override
    public void balance() {
        var balanceCommand = new BalanceCommand(cassettesVault);
        balanceCommand.execute();
    }
}
