package de.MCmoderSD.nasa.api.apod.data;

import tools.jackson.databind.JsonNode;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@SuppressWarnings("unused")
public abstract class APOD implements Serializable {

    // Attributes
    protected final Date date;
    protected final String title;
    protected final String explanation;
    protected final String version;
    protected final MediaType type;
    protected final String copyright;

    // Constructor
    public APOD(JsonNode data) {

        // Check Parameters
        if (data == null || data.isNull() || data.isEmpty()) throw new IllegalArgumentException("Data cannot be null or empty");

        // Init Attributes
        date = parseDate(data.get("date").asString());
        title = data.get("title").asString();
        explanation = data.get("explanation").asString();
        version = data.get("service_version").asString();
        type = MediaType.fromString(data.get("media_type").asString());

        // Init Optional Attributes
        copyright = data.has("copyright") ? data.get("copyright").asString() : null;
    }

    // Helper Method to Parse Date
    private static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format: " + date);
        }
    }

    // Getters
    public Date getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getVersion() {
        return version;
    }

    public MediaType getType() {
        return type;
    }

    public Optional<String> getCopyright() {
        return Optional.ofNullable(copyright);
    }

    // MediaType Enum
    public enum MediaType implements Serializable {

        // Values
        IMAGE, VIDEO;

        // Method to convert string to MediaType
        public static MediaType fromString(String type) {
            return switch (type.toLowerCase()) {
                case "image" -> IMAGE;
                case "video" -> VIDEO;
                default -> throw  new IllegalArgumentException("Unsupported media type: " + type);
            };
        }
    }
}