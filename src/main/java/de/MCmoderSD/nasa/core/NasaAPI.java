package de.MCmoderSD.nasa.core;

import de.MCmoderSD.nasa.api.apod.core.ApodAPI;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("unused")
public class NasaAPI {

    // Attributes
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    // Rate Limit Tracking
    private final AtomicInteger rateLimit;
    private final AtomicInteger rateLimitRemaining;

    // APIs
    private final ApodAPI apod;

    // Demo Constructor
    public NasaAPI() {
        this("DEMO_KEY");
    }

    // Constructor
    public NasaAPI(String apiKey) {

        // Check Parameters
        if (apiKey == null || apiKey.isEmpty()) throw new IllegalArgumentException("API key cannot be null or empty");

        // Initialize Attributes
        httpClient = HttpClient.newBuilder().build();
        objectMapper = new ObjectMapper();

        // Initialize Rate Limit Tracking
        rateLimit = new AtomicInteger(-1);
        rateLimitRemaining = new AtomicInteger(-1);

        // Initialize APIs
        apod = new ApodAPI(this, apiKey);
    }

    // Helper Method to Send Request
    public JsonNode sendRequest(HttpRequest request) {

        // Check Parameters
        if (request == null) throw new IllegalArgumentException("Request cannot be null");

        // Check Rate Limit
        if (rateLimit.get() == 0) throw new RuntimeException("Rate limit exceeded. Please wait for the limit to reset.");

        try {

            // Send Request
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Update Rate Limit Tracking
            response.headers().firstValue("X-RateLimit-Limit").ifPresent(v -> setIfParsable(rateLimit, v));
            response.headers().firstValue("X-RateLimit-Remaining").ifPresent(v -> setIfParsable(rateLimitRemaining, v));

            // Check Response Status
            if (response.statusCode() != 200) {
                throw new IOException("Request failed with status code: " + response.statusCode() + " and body: " + response.body());
            }

            // Parse Response Body
            return objectMapper.readTree(response.body());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to send request: " + e.getMessage(), e);
        }
    }

    // Helper Method to Set AtomicInteger if Parsable
    private static void setIfParsable(AtomicInteger target, String value) {
        try {
            target.set(Integer.parseInt(value.trim()));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Failed to parse rate limit value: " + value, e);
        }
    }

    // Rate Limit Getters
    public int getRateLimit() {
        return rateLimit.get();
    }

    public int getRateLimitRemaining() {
        return rateLimitRemaining.get();
    }

    // API Getters
    public ApodAPI apod() {
        return apod;
    }
}