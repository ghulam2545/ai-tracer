package com.ghulam.watch.service;

import com.ghulam.watch.dto.AnalyzeResponse;
import com.ghulam.watch.exception.AiParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class StackTraceAiClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(StackTraceAiClient.class);

    private static final String SYSTEM_PROMPT = """
            You are a senior production support engineer.
            Analyze the provided Java stack trace and return STRICTLY valid JSON with the following fields:
            - exceptionType   : fully-qualified exception class name
            - rootCause       : one-sentence description of the root cause
            - failurePoint    : class name and line number where the failure originated (e.g. "com.example.Foo:42")
            - severity        : one of LOW | MEDIUM | HIGH
            - suggestedFix    : JSON array of actionable remediation steps (strings)

            Do NOT return any explanation, markdown, or text outside the JSON object.
            Example output format:
            {
              "exceptionType": "java.lang.NullPointerException",
              "rootCause": "...",
              "failurePoint": "com.example.Service:88",
              "severity": "HIGH",
              "suggestedFix": ["Step 1", "Step 2"]
            }
            """;

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public StackTraceAiClient(ChatClient.Builder chatClientBuilder, ObjectMapper objectMapper) {
        this.chatClient   = chatClientBuilder.defaultSystem(SYSTEM_PROMPT).defaultAdvisors(new SimpleLoggerAdvisor()).build();
        this.objectMapper = objectMapper;
    }

    public AnalyzeResponse analyze(String stackTrace) {
        LOGGER.info("Sending stack trace to Ollama for analysis...");

        String rawResponse = chatClient
                .prompt()
                .user(stackTrace)
                .call()
                .content();

        LOGGER.debug("Raw AI response: {}", rawResponse);
        return parseResponse(rawResponse);
    }

    private AnalyzeResponse parseResponse(String raw) {
        try {
            String cleaned = raw.strip();
            if (cleaned.startsWith("```")) {
                cleaned = cleaned.replaceAll("^```[a-z]*\\n?", "").replaceAll("```$", "").strip();
            }
            return objectMapper.readValue(cleaned, AnalyzeResponse.class);
        } catch (Exception ex) {
            LOGGER.error("Failed to parse AI response as JSON. Raw response:\n{}", raw);
            throw new AiParsingException("AI returned a response that could not be parsed as JSON.", ex);
        }
    }
}