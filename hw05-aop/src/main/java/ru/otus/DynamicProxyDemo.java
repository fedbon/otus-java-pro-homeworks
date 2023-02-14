package ru.otus;

public class DynamicProxyDemo {
    public static void main(String[] args) throws ClassNotFoundException {

        var testLoggingClass = Ioc.createTestLoggingClass();
        testLoggingClass.calculation(Integer.MAX_VALUE);
        testLoggingClass.calculation(4, 10); //there is no logging here
        testLoggingClass.calculation(1, 8, "testParam");
        testLoggingClass.calculation("testParam");
    }
}
