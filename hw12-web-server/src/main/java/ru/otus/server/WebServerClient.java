package ru.otus.server;

public interface WebServerClient {
    void start() throws Exception;

    void join() throws Exception;

    void stop() throws Exception;
}
