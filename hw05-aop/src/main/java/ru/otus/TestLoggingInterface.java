package ru.otus;

public interface TestLoggingInterface {
    void calculation(int firstParam);

    void calculation(int firstParam, int secondParam);

    void calculation(int firstParam, int secondParam, String thirdParam);

    void calculation(String firstParam);
}
