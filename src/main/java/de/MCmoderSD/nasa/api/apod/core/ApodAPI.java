package de.MCmoderSD.nasa.api.apod.core;

import de.MCmoderSD.nasa.api.apod.data.APOD;
import de.MCmoderSD.nasa.api.apod.data.ImageAPOD;
import de.MCmoderSD.nasa.api.apod.data.VideoAPOD;
import de.MCmoderSD.nasa.core.NasaAPI;
import org.jetbrains.annotations.Nullable;
import tools.jackson.databind.JsonNode;

import java.net.URI;
import java.net.http.HttpRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import static de.MCmoderSD.nasa.api.apod.data.APOD.MediaType.fromString;

@SuppressWarnings("unused")
public class ApodAPI {

    // Constants
    private static final String ENDPOINT = "https://api.nasa.gov/planetary/apod";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    // Attributes
    private final NasaAPI api;
    private final String apiKey;

    // Constructor
    public ApodAPI(NasaAPI api, String apiKey) {
        this.api = api;
        this.apiKey = apiKey;
    }

    // Helper Methods
    private static String buildUrl(@Nullable Date date, @Nullable Date start, @Nullable Date end, @Nullable Integer count, String apiKey) {

        // Build URL
        var url = new StringBuilder(ENDPOINT + "?");

        // Append Date
        if (date != null) {
            url.append("date=").append(DATE_FORMAT.format(date)).append("&");
        }

        // Append Start
        if (start != null) {
            url.append("start_date=").append(DATE_FORMAT.format(start)).append("&");
        }

        // Append End
        if (end != null) {
            url.append("end_date=").append(DATE_FORMAT.format(end)).append("&");
        }

        // Append Count
        if (count != null) {
            url.append("count=").append(count).append("&");
        }

        // Append API Key
        return url.append("thumbs=true&api_key=").append(apiKey).toString();
    }

    private static APOD buildAPOD(JsonNode data) {

        // Check Parameters
        if (data == null || data.isNull() || data.isEmpty()) throw new IllegalArgumentException("Data cannot be null or empty");

        // Build APOD
        return switch (fromString(data.get("media_type").asString())) {
            case IMAGE -> new ImageAPOD(data);
            case VIDEO -> new VideoAPOD(data);
        };
    }

    // Fetch Methods
    public APOD fetch() {

        // Call API
        var url = buildUrl(null, null, null, null, apiKey);
        var request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        var response = api.sendRequest(request);

        // Build APOD
        return buildAPOD(response);
    }

    public APOD fetch(Date date) {

        // Call API
        var url = buildUrl(date, null, null, null, apiKey);
        var request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        var response = api.sendRequest(request);

        // Build APOD
        return buildAPOD(response);
    }

    public HashSet<APOD> fetch(int count) {

        // Call API
        var url = buildUrl(null, null, null, count, apiKey);
        var request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        var response = api.sendRequest(request);

        // Build APODs
        var apods = new HashSet<APOD>();
        for (var data : response) apods.add(buildAPOD(data));
        return apods;
    }

    public ArrayList<APOD> fetch(Date start, @Nullable Date end) {

        // Call API
        var url = buildUrl(null, start, end, null, apiKey);
        var request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        var response = api.sendRequest(request);

        // Build APODs
        var apods = new ArrayList<APOD>();
        for (var data : response) apods.add(buildAPOD(data));
        return apods;
    }
}