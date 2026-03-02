package com.ghulam.watch.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;

@Getter
@Entity
@Table(name = "incidents")
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String serviceName;

    @Column(nullable = false)
    private String environment;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String stackTrace;

    @Column(nullable = false)
    private String exceptionType;

    @Column(nullable = false)
    private String severity;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    // ── Constructors ──────────────────────────────────────────────────────────

    protected Incident() {
    }

    public Incident(String serviceName, String environment, String stackTrace,
                    String exceptionType, String severity) {
        this.serviceName = serviceName;
        this.environment = environment;
        this.stackTrace = stackTrace;
        this.exceptionType = exceptionType;
        this.severity = severity;
    }
}