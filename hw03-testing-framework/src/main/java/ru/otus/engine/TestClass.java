package ru.otus.engine;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;
import ru.otus.exception.TestingFrameworkException;
import ru.otus.service.ConsoleOutputService;
import ru.otus.service.OutputService;

public class TestClass {

    private final OutputService outputService = new ConsoleOutputService();
    private static final String PASSED_TEST_MESSAGE = "This method should pass the test";

    @Before
    public void beforeMethod() {
        String message = "beforeMethodCalled";
        outputService.outputMessage(message);
    }

    @Test
    public void firstTestingMethod() {
        outputService.outputMessage(PASSED_TEST_MESSAGE);
    }

    @Test
    public void firstFailedMethod() {
        String message = "This method should fail the test";
        throw new TestingFrameworkException(message);
    }

    @Test
    public void secondCorrectMethod() {
        outputService.outputMessage(PASSED_TEST_MESSAGE);
    }

    @Test
    public void secondFailedMethod() {
        throw new TestingFrameworkException("This method should fail the test");
    }

    @Test
    public void thirdCorrectMethod() {
        outputService.outputMessage(PASSED_TEST_MESSAGE);
    }

    @After
    public void afterMethod() {
        String message = "afterMethodCalled\n";
        outputService.outputMessage(message);
    }
}
