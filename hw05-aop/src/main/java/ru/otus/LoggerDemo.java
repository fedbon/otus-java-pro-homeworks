package ru.otus;

public class LoggerDemo {
    public static void main(String[] args) {
        var firstInterface = new Ioc<FirstInterface>(new LoggingClass()).createProxy();
        firstInterface.calculate(Integer.MAX_VALUE);
        firstInterface.calculate(1, 2);
        firstInterface.calculate("Test");  // there is no logging here
        firstInterface.calculate(5, 10, "Test");

        var secondInterface = new Ioc<SecondInterface>(new LoggingClass()).createProxy();
        secondInterface.doSomething(true);
        secondInterface.doSomething(Long.MIN_VALUE); // there is no logging here
    }
}
