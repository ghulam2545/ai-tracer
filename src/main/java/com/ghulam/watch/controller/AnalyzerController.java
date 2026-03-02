package com.ghulam.watch.controller;

import com.ghulam.watch.dto.AnalyzeRequest;
import com.ghulam.watch.dto.AnalyzeResponse;
import com.ghulam.watch.service.AnalyzerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping(path = "/api")
public class AnalyzerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzerController.class);
    private final AnalyzerService analyzerService;

    public AnalyzerController(AnalyzerService analyzerService) {
        this.analyzerService = analyzerService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<AnalyzeResponse> analyze(@Valid @RequestBody AnalyzeRequest request) {
        LOGGER.info("POST /api/analyze received for service ={}", request.serviceName());
        AnalyzeResponse response = analyzerService.analyze(request);
        return ResponseEntity.ok(response);
    }
}
