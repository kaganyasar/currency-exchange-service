package com.example.microservices.currencyexchangeservice.controller;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Resilience4jController {

    private Logger logger = LoggerFactory.getLogger(Resilience4jController.class);

    @GetMapping("/sample-api-retry")
    @Retry(name = "sample-retry", fallbackMethod = "hardCodedResponse")
    public String sampleApiRetry() {
        logger.info("Sample Api retry call received");
        ResponseEntity<String> forEntity = new RestTemplate().getForEntity("http://localhost:8080/some-dummy-url", String.class);
        return forEntity.getBody();
    }

    @GetMapping("/sample-api-circuit-breaker")
    @CircuitBreaker(name = "sample-circuit-breaker", fallbackMethod = "hardCodedResponse")
    public String sampleApiCircuitBreaker() {
        logger.info("Sample Api circuit breaker call received");
        ResponseEntity<String> forEntity = new RestTemplate().getForEntity("http://localhost:8080/some-dummy-url", String.class);
        return forEntity.getBody();
    }

    @GetMapping("/sample-api-rate-limiter")
    @RateLimiter(name = "sample-rate-limiter")
    public String sampleApiRateLimiter() {
        logger.info("Sample Api rate limiter call received");
        return "sample-api-rate-limiter response";
    }

    @GetMapping("/sample-api-bulkhead")
    @Bulkhead(name = "sample-bulkhead")
    public String sampleApiBulkhead() {
        logger.info("Sample Api bulkhead call received");
        return "sample-api-bulkhead response";
    }

    public String hardCodedResponse(Exception ex) {
        return "fallback-response";
    }
}
