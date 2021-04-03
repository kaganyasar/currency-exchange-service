package com.example.microservices.currencyexchangeservice.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CircuitBreakerController {

    private Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);

    @GetMapping("/sample-api-retry")
    @Retry(name = "sample-api-retry", fallbackMethod = "hardCodedResponse")
    public String sampleApiRetry() {
        logger.info("Sample Api retry call received");
        ResponseEntity<String> forEntity = new RestTemplate().getForEntity("http://localhost:8080/some-dummy-url", String.class);
        return forEntity.getBody();
    }

    @GetMapping("/sample-api-circuit-breaker")
    @CircuitBreaker(name = "default", fallbackMethod = "hardCodedResponse")
    public String sampleApiCircuitBreaker() {
//        logger.info("Sample Api circuit breaker call received");
        logger.info("X");
        ResponseEntity<String> forEntity = new RestTemplate().getForEntity("http://localhost:8080/some-dummy-url", String.class);
        return forEntity.getBody();
    }

    public String hardCodedResponse(Exception ex) {
        logger.info("Y");
        return "fallback-response";
    }
}
