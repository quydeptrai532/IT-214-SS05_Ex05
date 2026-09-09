package com.vietmart.search1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Search Service V1 — phiên bản ỔN ĐỊNH (stable), chiếm 80% lưu lượng.
 */
@SpringBootApplication
public class SearchServiceV1Application {
    public static void main(String[] args) {
        SpringApplication.run(SearchServiceV1Application.class, args);
    }
}