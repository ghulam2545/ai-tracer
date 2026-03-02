package com.ghulam.watch.dto;

import java.util.List;

public record AnalyzeResponse(

        String exceptionType,
        String rootCause,
        String failurePoint,
        String severity,
        List<String> suggestedFix
) {}