package ru.otus.listener;

import ru.otus.model.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message> messagesMap = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        messagesMap.put(msg.getId(), new Message(msg));
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(messagesMap.get(id));
    }
}
