package ru.otus.processor;

import ru.otus.model.Message;

public class ProcessorSwapFields11and12 implements Processor {

    @Override
    public Message process(Message message) {
        var tmp = message.getField12();
        return message.toBuilder().field12(message.getField11()).field11(tmp).build();
    }
}
