package de.MCmoderSD.nasa.api.apod.data;

import tools.jackson.databind.JsonNode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@SuppressWarnings("unused")
public class VideoAPOD extends APOD implements Serializable {

    // Attributes
    private final URL video;
    private final URL thumbnail;

    // Constructor
    public VideoAPOD(JsonNode data) {

        // Check Parameters
        if (data == null || data.isNull() || data.isEmpty()) throw new IllegalArgumentException("Data cannot be null or empty");

        // Call Super
        super(data);

        // Init URLs
        video = parseUrl(data.get("url").asString());
        thumbnail = parseUrl(data.get("thumbnail_url").asString());
    }

    // Helper Method to Parse URL
    private static URL parseUrl(String urlString) {
        try {
            return new URI(urlString).toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL: " + urlString, e);
        }
    }

    // Getters
    public URL getVideoUrl() {
        return video;
    }

    public URL getThumbnailUrl() {
        return thumbnail;
    }

    public BufferedImage getThumbnail() throws IOException {
        return ImageIO.read(new BufferedInputStream(thumbnail.openStream()));
    }
}