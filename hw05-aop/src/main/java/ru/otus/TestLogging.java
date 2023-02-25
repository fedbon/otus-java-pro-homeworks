package ru.otus;

public class TestLogging implements FirstTestLoggingInterface, SecondTestLoggingInterface {
    @Log
    @Override
    public void calculate(int param) {

    }

    @Log
    @Override
    public void calculate(int firstParam, int secondParam) {

    }

    @Log
    @Override
    public void calculate(int firstParam, int secondParam, String thirdParam) {

    }

    @Override
    public void calculate(String param) {

    }

    @Log
    @Override
    public void doSomething(boolean param) {

    }

    @Override
    public void doSomething(long param) {

    }
}
