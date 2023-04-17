package ru.otus.crm.service;

import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.cache.CacheEngineImpl;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;


import java.util.List;

class CEPerformanceTest {

    private DBServiceClient dbServiceClientWithoutCache;
    private DBServiceClient dbServiceClientWithCache;

    @BeforeEach
    void init() {
        var configuration = new Configuration().configure("test.hibernate.cfg.xml");
        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        dbServiceClientWithoutCache = new DBServiceClientImpl(transactionManager, clientTemplate);
        dbServiceClientWithCache = new DBServiceClientWithCacheImpl(
                new DBServiceClientImpl(transactionManager, clientTemplate),
                new CacheEngineImpl<>());
    }

    private double timeInMillis(Runnable runnable) {
        double start = System.nanoTime();
        runnable.run();
        return (System.nanoTime() - start) / 1000_000;
    }

    @DisplayName("производительность выше с кешем, чем без")
    @Test
    void withCacheFasterThanWithoutCache() {
        for (int i = 1; i < 3_000; i++) {
            dbServiceClientWithoutCache.saveClient(new Client(null, "Client" + i,
                    new Address(null, "Grove Street"),
                    List.of(new Phone(null, "8-800-555-22"),
                            new Phone(null, "8-800-555-40"))));
        }

        var withoutCacheTime = timeInMillis(() -> dbServiceClientWithoutCache.findAll()) / 1_000;
        var withCacheTime = timeInMillis(() -> dbServiceClientWithCache.findAll()) / 1_000;
        System.out.printf("Without cache %f millis %n", withoutCacheTime);
        System.out.printf("With cache %f millis %n", withCacheTime);
        System.out.printf("With cache %f times faster than without cache %n", (withoutCacheTime / withCacheTime));
        Assertions.assertTrue(withCacheTime < withoutCacheTime);
    }
}