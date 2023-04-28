package ru.otus;

import org.hibernate.cfg.Configuration;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClientImpl;
import ru.otus.dao.InMemoryAdminDao;
import ru.otus.server.WebServerClientSecuredImpl;
import ru.otus.services.AuthServiceImpl;
import ru.otus.services.TemplateProcessorImpl;


/*
    // Стартовая страница
    http://localhost:8080

    http://localhost:8080/clients

    http://localhost:8080/api/client
*/

public class WebServerDemo {

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();
        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class,
                Address.class, Phone.class);
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        var dbServiceClient = new DBServiceClientImpl(transactionManager, clientTemplate);

        var adminDao = new InMemoryAdminDao();
        var templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        var authService = new AuthServiceImpl(adminDao);

        var webServerClient = new WebServerClientSecuredImpl(WEB_SERVER_PORT,
                authService, dbServiceClient, templateProcessor);

        webServerClient.start();
        webServerClient.join();
    }
}
