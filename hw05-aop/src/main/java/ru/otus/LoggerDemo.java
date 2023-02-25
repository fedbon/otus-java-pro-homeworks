package ru.otus;

public class LoggerDemo {
    public static void main(String[] args) {
        Ioc.<FirstTestLoggingInterface>createProxy(TestLogging.class).calculate(Integer.MAX_VALUE);
        Ioc.<FirstTestLoggingInterface>createProxy(TestLogging.class).calculate(6, 10);
        Ioc.<FirstTestLoggingInterface>createProxy(TestLogging.class).calculate("Test");  // there is no logging here
        Ioc.<FirstTestLoggingInterface>createProxy(TestLogging.class).calculate(5, 10, "Test");

        Ioc.<SecondTestLoggingInterface>createProxy(TestLogging.class).doSomething(true);
        Ioc.<SecondTestLoggingInterface>createProxy(TestLogging.class).doSomething(Long.MAX_VALUE); // there is no logging here
    }
}
