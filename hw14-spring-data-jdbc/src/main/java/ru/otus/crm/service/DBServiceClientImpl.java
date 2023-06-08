package ru.otus.crm.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.crm.repository.ClientRepository;
import ru.otus.sessionmanager.TransactionManager;
import ru.otus.crm.model.Client;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DBServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DBServiceClientImpl.class);

    private final ClientRepository clientRepository;
    private final TransactionManager transactionManager;

    @Override
    public List<Client> findAll() {
        var clientList = clientRepository.findAll();
        log.info("all clients: {}", clientList);
        return clientList;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(() -> {
            var savedClient = clientRepository.save(client);
            log.info("saved client: {}", savedClient);
            return savedClient;
        });
    }
}
