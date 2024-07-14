package com.example.shetuanlianmeng.controller;

import com.example.shetuanlianmeng.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping
    public Map<String, Long> getStatistics() {
        return statisticsService.getStatistics();
    }
}
