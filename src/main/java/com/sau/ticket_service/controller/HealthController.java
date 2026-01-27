package com.sau.ticket_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HealthController {

  @Operation(summary = "Test: @GetMapping")
  @GetMapping("/health")
  public String health() {
    return "ok";
  }

  @Operation(summary = "Test: @PostMapping")
  @PostMapping("/ping")
  public String ping() {
    return "pong";
  }
}
