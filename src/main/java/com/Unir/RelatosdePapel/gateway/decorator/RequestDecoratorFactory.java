package com.Unir.RelatosdePapel.gateway.decorator;

import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;

import com.Unir.RelatosdePapel.gateway.model.GatewayRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RequestDecoratorFactory {
    private final ObjectMapper objectMapper;

    public ServerHttpRequestDecorator getDecorator(GatewayRequest gatewayRequest) {
        return switch (gatewayRequest.getTargetMethod().name().toUpperCase()) {
            case "GET" -> new GetRequestDecorator(gatewayRequest);
            case "POST" -> new PostRequestDecorator(gatewayRequest, objectMapper);
            case "PUT" -> new PutRequestDecorator(gatewayRequest, objectMapper);
            case "PATCH" -> new PatchRequestDecorator(gatewayRequest, objectMapper);
            case "DELETE" -> new DeleteRequestDecorator(gatewayRequest);
            default -> throw new IllegalArgumentException("Unsupported HTTP method");
        };
    }
}
