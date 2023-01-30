package ru.otus;

import ru.otus.engine.TestRunner;

public class Main {
    public static void main(String... args) throws Exception {
        TestRunner testRunner = new TestRunner();
        testRunner.runTests("ru.otus.engine.TestClass");
    }
}
