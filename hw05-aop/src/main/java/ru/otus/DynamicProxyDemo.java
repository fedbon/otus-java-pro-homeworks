package ru.otus;

public class DynamicProxyDemo {
    public static void main(String[] args) {

        var testLogging = Ioc.createTestLoggingClass();
        testLogging.calculation(Integer.MAX_VALUE);
        testLogging.calculation(4, 10); //there is no logging here
        testLogging.calculation(1, 8, "testParam");
        testLogging.calculation("testParam");
    }
}
