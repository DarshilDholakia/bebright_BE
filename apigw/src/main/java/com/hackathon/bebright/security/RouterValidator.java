package com.hackathon.bebright.security;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

// This class decides whether a request should contain a token or not.
@Component
public class RouterValidator {


    // Defining the list of endpoints that do not require a token
    public static final List<String> openApiEndpoints= List.of(
            "/auth"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
