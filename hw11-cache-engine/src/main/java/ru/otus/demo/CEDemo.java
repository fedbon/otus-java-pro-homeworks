package ru.otus.demo;

import org.hibernate.cfg.Configuration;
import ru.otus.cache.CacheEngineImpl;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClientWithCacheImpl;
import ru.otus.crm.service.DBServiceClientImpl;

import java.util.List;

public class CEDemo {

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
///
        var dbServiceClientWithCache = new DBServiceClientWithCacheImpl(
                new DBServiceClientImpl(transactionManager, clientTemplate),
                new CacheEngineImpl<>());
        for (int i = 1; i < 500; i++) {
            dbServiceClientWithCache.saveClient(new Client(null, "Client" + i,
                    new Address(null, "Grove Street"),
                    List.of(new Phone(null, "8-800-555-22"),
                    new Phone(null, "8-800-555-40"))));
        }
        dbServiceClientWithCache.getClient(3L);
        dbServiceClientWithCache.findAll();
    }
}
