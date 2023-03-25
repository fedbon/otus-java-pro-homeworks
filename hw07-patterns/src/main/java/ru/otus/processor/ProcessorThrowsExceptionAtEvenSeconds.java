package ru.otus.processor;

import ru.otus.exception.EvenSecondException;
import ru.otus.model.Message;

public class ProcessorThrowsExceptionAtEvenSeconds implements Processor {

    private final DateTimeProvider dateTimeProvider;

    public ProcessorThrowsExceptionAtEvenSeconds(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }
    @Override
    public Message process(Message message) {
        if (dateTimeProvider.getDate().getSecond() % 2 == 0) {
            throw new EvenSecondException("It's even second!");
        }
        return message;
    }
}
