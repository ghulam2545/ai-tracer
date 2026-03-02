package com.ghulam.watch.service;

import com.ghulam.watch.dto.AnalyzeRequest;
import com.ghulam.watch.dto.AnalyzeResponse;
import com.ghulam.watch.entity.Incident;
import com.ghulam.watch.repo.IncidentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnalyzerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzerService.class);

    private final StackTraceAiClient aiClient;
    private final IncidentRepository incidentRepository;

    public AnalyzerService(StackTraceAiClient aiClient, IncidentRepository incidentRepository) {
        this.aiClient           = aiClient;
        this.incidentRepository = incidentRepository;
    }

    @Transactional
    public AnalyzeResponse analyze(AnalyzeRequest request) {
        LOGGER.info("Analyzing stack trace for service = '{}' env = '{}'",
                request.serviceName(), request.environment());

        AnalyzeResponse response = aiClient.analyze(request.stackTrace());
        Incident incident = new Incident(
                request.serviceName(),
                request.environment(),
                request.stackTrace(),
                response.exceptionType(),
                response.severity()
        );
        Incident saved = incidentRepository.save(incident);
        LOGGER.info("Incident saved with id = {}", saved.getId());

        return response;
    }
}
