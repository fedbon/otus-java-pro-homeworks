package ru.otus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.dto.ClientDto;

@Controller
@RequiredArgsConstructor
public class ClientController {

    private final DBServiceClient clientService;

    @GetMapping("/clients")
    public String clientsListView(Model model) {
        if (model.getAttribute("client") == null) {
            model.addAttribute("client", new ClientDto());
        }
        model.addAttribute("clients", clientService.findAll());
        return "clients";
    }

    @PostMapping("/clients")
    public RedirectView saveClient(ClientDto dto) {
        clientService.saveClient(new Client(dto));
        return new RedirectView("/clients", true);
    }
}
