package ru.otus.processor;

import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProcessorThrowsExceptionAtEvenSecondsTest {
    @Test
    void shouldThrowException() {
        var evenSecond = LocalDateTime.parse("2023-03-20T11:50:56");

        var exception = assertThrows(RuntimeException.class,
                () -> new ProcessorThrowsExceptionAtEvenSeconds(() -> evenSecond)
                        .process(new Message.Builder(1L).build()));

        var expectedMessage = "It's even second!";
        var actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldNotThrowException() {
        var oddSecond = LocalDateTime.parse("2023-03-20T11:50:55");

        assertDoesNotThrow(() -> new ProcessorThrowsExceptionAtEvenSeconds(() -> oddSecond)
                .process(new Message.Builder(1L).build()));
    }
}
