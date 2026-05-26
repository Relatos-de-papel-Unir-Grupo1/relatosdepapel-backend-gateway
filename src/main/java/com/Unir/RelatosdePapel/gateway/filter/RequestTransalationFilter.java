package com.Unir.RelatosdePapel.gateway.filter;


import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.Unir.RelatosdePapel.gateway.decorator.RequestDecoratorFactory;
import com.Unir.RelatosdePapel.gateway.model.GatewayRequest;
import com.Unir.RelatosdePapel.gateway.utils.RequestBodyExtractor;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class RequestTransalationFilter implements GlobalFilter {

    private final RequestBodyExtractor RequestBodyExtractor;
    private final RequestDecoratorFactory requestDecoratorFactory;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Starting request translation filter");
        exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);

        if (exchange.getRequest().getHeaders().getContentType() == null || !exchange.getRequest().getMethod().equals(HttpMethod.POST)) {
            log.warn("Invalid request: missing content type or not POST method");
            return exchange.getResponse().setComplete();
        }
        else {
            return DataBufferUtils.join(exchange.getRequest().getBody())
                .flatMap(buffer -> {
                    try {
                        GatewayRequest request = RequestBodyExtractor.getRequest(exchange, buffer);
                        log.info("Request translation successful: {}", request);
                        ServerHttpRequest mutatedRequest = requestDecoratorFactory.getDecorator(request);

                        exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, mutatedRequest.getURI());
                        if (request.getQueryParams() != null) {
                            request.getQueryParams().clear();
                        }
                        log.info("Proxying request: {} {}", mutatedRequest.getMethod(), mutatedRequest.getURI());
                        return chain.filter(exchange.mutate().request(mutatedRequest).build());
                    } catch (Exception e) {
                        log.error("Error during request translation", e);
                        exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                        return exchange.getResponse().setComplete();
                    }
                });
        }
    }

}
