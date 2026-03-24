package com.github.alfredobaptista.entrypoint.controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1")
public class FakeStripeController {

    @PostMapping("/charges")
    public Map<String, Object> charge(
            @RequestBody Map<String, Object> request,
            @RequestParam(defaultValue = "") String scenario
    ) throws InterruptedException {

        // 🔥 Simular timeout (slow call)
        if ("timeout".equalsIgnoreCase(scenario)) {
            Thread.sleep(3000); // maior que 2s → ativa slowCall
        }


        // 🔥 Simular erro (falha)
        if ("error".equalsIgnoreCase(scenario)) {
            throw new RuntimeException("Gateway error simulado");
        }

        return Map.of(
                "id",  UUID.randomUUID().toString(),
                "status", "succeeded",
                "amount", request.getOrDefault("amount", 1000),
                "currency", request.getOrDefault("currency", "usd"),
                "created", LocalDateTime.now().toString()
        );
    }
}