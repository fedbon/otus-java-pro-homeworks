package ru.otus.service;

import java.io.PrintStream;

public class ConsoleOutputService implements OutputService {

    private final PrintStream out;

    public ConsoleOutputService() {
        out = System.out;
    }

    @Override
    public void outputMessage(String message) {
        out.println(message);
    }

    @Override
    public void printBorder() {
        out.println("*****************************************");
    }

    @Override
    public void printTotalNumberOfTests(int totalNumber) {
        out.printf("TOTAL NUMBER OF TESTS: %d%n", totalNumber);
    }

    @Override
    public void printPassedTests(int passedTests) {
        out.printf("TESTS PASSED: %d%n", passedTests);
    }

    @Override
    public void printFailedTests(int failedTests) {
        out.printf("TESTS FAILED: %d%n", failedTests);
    }
}
