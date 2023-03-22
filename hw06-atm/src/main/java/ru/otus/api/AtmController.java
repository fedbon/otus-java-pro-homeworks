package ru.otus.api;

import java.util.List;

public interface AtmController {
    void withdraw(int amount);

    void deposit(List<Bill> bills);

    void balance();
}
