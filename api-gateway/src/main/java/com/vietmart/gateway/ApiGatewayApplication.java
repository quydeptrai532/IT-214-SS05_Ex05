package com.vietmart.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * API Gateway (Spring Cloud Gateway).
 *
 * Định tuyến A/B Testing cho /api/search bằng Weight Route Predicate:
 *   route search-service-v1 (weight 8)  → http://localhost:8081  → trả "V1"
 *   route search-service-v2 (weight 2)  → http://localhost:8082  → trả "V2"
 *
 * Request /api/search được điều hướng xác suất theo tỷ lệ 80% : 20%.
 */
@SpringBootApplication
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}