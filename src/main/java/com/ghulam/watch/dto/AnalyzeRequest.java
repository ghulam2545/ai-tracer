package com.ghulam.watch.dto;

import jakarta.validation.constraints.NotBlank;

public record AnalyzeRequest(

        @NotBlank(message = "serviceName must not be blank")
        String serviceName,

        @NotBlank(message = "environment must not be blank")
        String environment,

        @NotBlank(message = "stackTrace must not be blank")
        String stackTrace
) {}