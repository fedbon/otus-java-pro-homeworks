package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.ProcessorSwapFields11and12;
import ru.otus.processor.ProcessorThrowsExceptionAtEvenSeconds;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PatternsDemo {
    public static void main(String[] args) {
        var processors = List.of(new ProcessorSwapFields11and12(),
                new ProcessorThrowsExceptionAtEvenSeconds(LocalDateTime::now));

        var complexProcessor = new ComplexProcessor(processors, ex -> {});
        var historyListener = new HistoryListener();
        complexProcessor.addListener(historyListener);

        var data = "initialField13";
        var field13 = new ObjectForMessage();
        var field13Data = new ArrayList<String>();
        field13Data.add(data);
        field13.setData(field13Data);

        var message = new Message.Builder(1L)
                .field11("field11")
                .field12("field12")
                .field13(field13)
                .build();

        System.out.println("oldMsg:" + message);

        message.getField13().setData(new ArrayList<>(Collections.singleton("resultField13")));
        var result = complexProcessor.handle(message);

        System.out.println("result:" + result);

        complexProcessor.removeListener(historyListener);
    }
}
