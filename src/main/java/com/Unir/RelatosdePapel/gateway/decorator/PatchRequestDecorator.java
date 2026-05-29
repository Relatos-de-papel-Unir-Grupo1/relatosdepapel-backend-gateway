package com.Unir.RelatosdePapel.gateway.decorator;

import java.net.URI;

import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.util.UriComponentsBuilder;

import com.Unir.RelatosdePapel.gateway.model.GatewayRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;


@Slf4j
public class PatchRequestDecorator extends ServerHttpRequestDecorator {
  final GatewayRequest gatewayRequest;
    private final ObjectMapper objectMapper;

    public PatchRequestDecorator(GatewayRequest gatewayRequest, ObjectMapper objectMapper) {
        super(gatewayRequest.getExchange().getRequest());
        this.gatewayRequest = gatewayRequest;
        this.objectMapper = objectMapper;
    }

    @Override
    @NonNull
    public HttpMethod getMethod() {
        log.info("Decorating PATCH request");
        return HttpMethod.PATCH;
    }

    @Override
    @NonNull
    public URI getURI() {
        log.info("Decorating URI for PATCH request");
        return UriComponentsBuilder.fromUri((URI) gatewayRequest.getExchange().getAttributes().get(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR))
                .build()
                .toUri();
    }

    @Override
    @NonNull
    public HttpHeaders getHeaders() {
        return gatewayRequest.getHeaders();
    }

    @Override
    @NonNull
    @SneakyThrows
    public Flux<DataBuffer> getBody() {
        log.info("Decorating body for PATCH request");
        DataBufferFactory bufferFactory = new DefaultDataBufferFactory();
        byte[] bodyData = objectMapper.writeValueAsBytes(gatewayRequest.getBody());
        DataBuffer buffer = bufferFactory.allocateBuffer(bodyData.length);
        buffer.write(bodyData);
        return Flux.just(buffer);
    }
}
