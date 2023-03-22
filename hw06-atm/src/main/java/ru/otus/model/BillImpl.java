package ru.otus.model;

import ru.otus.api.Bill;

public record BillImpl(Denomination denomination) implements Bill {

    @Override
    public String toString() {
        return denomination.value() + " RUB bill";
    }
}
