package ru.otus.api;

import ru.otus.model.Denomination;

import java.util.List;

public interface AtmCassette {
    Denomination denomination();

    void put(List<Bill> bills);

    Bill take();

    int count();
}
