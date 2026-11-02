package com.likelion.springsession.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    record HealthResponse(String status, String message) {}

    @GetMapping("/api/health")
    public HealthResponse health() {
        return new HealthResponse("UP", "멋사 14기 스프링 서버가 자동 배포됐습니다!! 🦁");
    }
}