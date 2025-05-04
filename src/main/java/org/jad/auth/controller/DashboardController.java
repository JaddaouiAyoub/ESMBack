package org.jad.auth.controller;

import lombok.RequiredArgsConstructor;
import org.jad.auth.dto.DashboardDTO;
import org.jad.auth.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public DashboardDTO getStats() {
        return dashboardService.getDashboardData();
    }
}
