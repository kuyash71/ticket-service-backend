package com.sau.ticket_service.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public String health() { return "ok"; }

    @PostMapping("/ping")
    public String ping() { return "pong"; }
}
