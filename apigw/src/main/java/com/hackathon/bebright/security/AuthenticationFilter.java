package com.hackathon.bebright.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    @Autowired
    private final RouterValidator routerValidator;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtConfig jwtConfig;

    public AuthenticationFilter(RouterValidator routerValidator, JwtTokenUtil jwtTokenUtil, JwtConfig config) {
        super(Config.class);
        this.routerValidator = routerValidator;
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtConfig = config;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (routerValidator.isSecured.test(exchange.getRequest()) && !jwtConfig.isAuthDisabled()) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Missing Authorisation Header");
                }

                String authHeader = Objects.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
                try {
                    jwtTokenUtil.validateToken(authHeader);
                }
                catch (Exception ex) {
                    log.error("Error Validating Authentication Header", ex);
//                    List<String> details = new ArrayList<>();
//                    details.add(ex.getLocalizedMessage());
//                    ErrorResponseDto error = new ErrorResponseDto(new Date(), HttpStatus.UNAUTHORIZED.value(), "UNAUTHORIZED", details, exchange.getRequest().getURI().toString());
//                    ServerHttpResponse response = exchange.getResponse();
//
//                    byte[] bytes = SerializationUtils.serialize(error);
//
//                    DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
//                    response.writeWith(Flux.just(buffer));
//                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
//                    return response.setComplete();
                }
            }

            return chain.filter(exchange);
        });
    }

    public static class Config {
    }
}
