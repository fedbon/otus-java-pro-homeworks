package ru.otus;

public class TestLogging implements TestLoggingInterface {
    @Log
    @Override
    public void calculation(int firstParam) {

    }

    @Override
    public void calculation(int firstParam, int secondParam) {

    }

    @Log
    @Override
    public void calculation(int firstParam, int secondParam, String thirdParam) {

    }

    @Log
    @Override
    public void calculation(String firstParam) {

    }
}
