package com.vietmart.search2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Search Service V2 — phiên bản THỬ NGHIỆM (candidate), chiếm 20% lưu lượng.
 * Dùng thuật toán tìm kiếm tối ưu hơn (chỉ là giả lập).
 */
@SpringBootApplication
public class SearchServiceV2Application {
    public static void main(String[] args) {
        SpringApplication.run(SearchServiceV2Application.class, args);
    }
}