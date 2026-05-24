package com.finance.manager.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class HealthPingScheduler {

    private static final Logger logger = LoggerFactory.getLogger(HealthPingScheduler.class);

    @Value("${APP_URL:http://localhost:8080}")
    private String appUrl;

    // Ping every 14 minutes to prevent Render from sleeping
    @Scheduled(fixedRate = 14 * 60 * 1000)
    public void pingBackend() {
        try {
            // Try external URL first, fallback to localhost
            String url = getPingUrl();
            
            RestTemplate rt = new RestTemplate();
            rt.getForEntity(url, String.class);
            logger.info("Health ping sent to prevent sleep: {}", url);
        } catch (Exception e) {
            logger.warn("Health ping failed: {}", e.getMessage());
        }
    }

    private String getPingUrl() {
        // If APP_URL env var is set (for Render), use it
        String envUrl = System.getenv("APP_URL");
        if (envUrl != null && !envUrl.isEmpty()) {
            return envUrl + "/api/health";
        }
        
        // Otherwise use localhost
        try {
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            return "http://" + hostAddress + ":8080/api/health";
        } catch (UnknownHostException e) {
            return "http://localhost:8080/api/health";
        }
    }
}