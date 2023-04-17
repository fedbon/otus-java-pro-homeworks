package ru.otus.crm.service;

import ru.otus.cache.CacheEngine;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

public class DBServiceClientWithCacheImpl implements DBServiceClient {
    private final DBServiceClient dbServiceClient;
    private final CacheEngine<String, Client> cacheEngine;

    public DBServiceClientWithCacheImpl(DBServiceClient dbServiceClient, CacheEngine<String, Client> cacheEngine) {
        this.dbServiceClient = dbServiceClient;
        this.cacheEngine = cacheEngine;
    }

    @Override
    public Client saveClient(Client client) {
        Client savedClient = dbServiceClient.saveClient(client.clone());
        cacheEngine.put(String.valueOf(savedClient.getId()), savedClient);
        return savedClient;
    }

    @Override
    public Optional<Client> getClient(long id) {
        Client client = cacheEngine.get(String.valueOf(id));
        if (client != null) {
            return Optional.of(client.clone());
        } else {
            Optional<Client> clientFromDB = dbServiceClient.getClient(id);
            clientFromDB.ifPresent(value -> cacheEngine.put(String.valueOf(value.getId()), value.clone()));
            return clientFromDB;
        }
    }

    @Override
    public List<Client> findAll() {
        return dbServiceClient.findAll();
    }
}
