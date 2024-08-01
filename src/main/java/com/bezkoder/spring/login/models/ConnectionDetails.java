package com.bezkoder.spring.login.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



@Entity
@Table(name = "connection_details")
public class ConnectionDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    private String status;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    public ConnectionDetails() {}

    public ConnectionDetails(String status, LocalDateTime timestamp) {
        this.status = status;
        this.timestamp = timestamp;
    }

    // Getters and setters
}
