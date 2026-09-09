package com.vietmart.search2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Giả lập API search — trả về "V2" để kiểm chứng A/B Testing qua Gateway.
 */
@RestController
public class SearchController {

    @GetMapping("/api/search")
    public String search() {
        return "V2";
    }
}