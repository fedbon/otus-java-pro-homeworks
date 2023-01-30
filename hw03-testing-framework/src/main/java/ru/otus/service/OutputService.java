package ru.otus.service;

public interface OutputService {
    void outputMessage (String message);
    void printBorder();
    void printTotalNumberOfTests(int totalNumber);
    void printPassedTests(int passedTests);
    void printFailedTests(int failedTests);
}
