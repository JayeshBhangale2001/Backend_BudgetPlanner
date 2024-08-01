package com.bezkoder.spring.login.controllers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.bezkoder.spring.login.models.ConnectionDetails;
import com.bezkoder.spring.login.repository.ConnectionDetailsRepository;

@Controller
public class StatusController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ConnectionDetailsRepository connectionDetailsRepository;

    @GetMapping("/")
    public String index(Model model) {
        String dbStatus;
        try {
            jdbcTemplate.execute("SELECT 1 FROM DUAL");
            dbStatus = "Connected to the database successfully";
            ConnectionDetails connectionDetails = new ConnectionDetails("Connected", LocalDateTime.now());
            connectionDetailsRepository.save(connectionDetails);
        } catch (Exception e) {
            dbStatus = "Failed to connect to the database: " + e.getMessage();
            e.printStackTrace();
            ConnectionDetails connectionDetails = new ConnectionDetails("Failed", LocalDateTime.now());
            connectionDetailsRepository.save(connectionDetails);
        }
        model.addAttribute("serverStatus", "Tomcat server started on port 8080");
        model.addAttribute("dbStatus", dbStatus);
        return "status";
    }
}
