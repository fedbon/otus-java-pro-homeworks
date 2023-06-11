package ru.otus.crm.service;

import ru.otus.crm.model.Client;
import java.util.List;

public interface DBServiceClient {
    List<Client> findAll();

    Client saveClient(Client client);

}
