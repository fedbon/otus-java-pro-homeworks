package ru.otus.model;

import ru.otus.api.AtmCassette;
import ru.otus.api.Bill;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.List;

public class AtmCassetteImpl implements AtmCassette {
    private final Denomination denomination;
    private final Deque<Bill> bills;

    public AtmCassetteImpl(Denomination denomination, Collection<Bill> bills) {
        this.denomination = denomination;
        this.bills = new ArrayDeque<>(bills);
    }

    @Override
    public void put(List<Bill> bills) {
        for (var bill : bills) {
            this.bills.push(bill);
        }
    }

    @Override
    public Bill take() {
        return bills.pop();
    }

    @Override
    public int count() {
        return bills.size();
    }

    @Override
    public Denomination denomination() {
        return denomination;
    }

    @Override
    public String toString() {
        return denomination().value() + " RUB(" + count() + " bills)";
    }
}
