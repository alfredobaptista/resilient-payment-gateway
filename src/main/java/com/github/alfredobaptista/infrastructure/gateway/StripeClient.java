package com.github.alfredobaptista.infrastructure.gateway;

import com.github.alfredobaptista.infrastructure.config.StripeConfig;
import com.github.alfredobaptista.infrastructure.dto.request.StripeChargeRequest;
import com.github.alfredobaptista.infrastructure.dto.response.StripeChargeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "stripe",
        url = "${gateway.stripe.url}",          // ← URL via propriedade
        configuration = StripeConfig.class      // ← interceptor de auth aplicado só aqui
)
public interface StripeClient {

    @PostMapping("/v1/charges")
    StripeChargeResponse charge(@RequestBody StripeChargeRequest request);
}