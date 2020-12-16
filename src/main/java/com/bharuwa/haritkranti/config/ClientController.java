package com.bharuwa.haritkranti.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/client")
public class ClientController {



    @GetMapping
    public String getClient() {
        return "OK " + LocalDateTime.now();
    }
}
