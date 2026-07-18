import de.MCmoderSD.nasa.api.apod.data.ImageAPOD;
import de.MCmoderSD.nasa.api.apod.data.VideoAPOD;
import de.MCmoderSD.nasa.core.NasaAPI;

import static java.lang.IO.println;

void main() {

    // API Key
    var apiKey = "DEMO_KEY"; // Replace with your own API key from https://api.nasa.gov/

    // Initialize NasaAPI
    var api = new NasaAPI(apiKey);

    // Request APOD
    var apod = api.apod().fetch();

    // Print APOD Data
    println("Title: " + apod.getTitle());
    println("Date: " + apod.getDate());
    println("Explanation: " + apod.getExplanation());
    println("Version: " + apod.getVersion());
    println("Copyright: " + apod.getCopyright().orElse("None"));

    switch (apod.getType()) {
        case IMAGE -> {
            var imageApod = (ImageAPOD) apod;
            println("Image URL: " + imageApod.getImageUrl());
            println("Thumbnail URL: " + imageApod.getThumbnailUrl());
        }
        case VIDEO -> {
            var videoApod = (VideoAPOD) apod;
            println("Video URL: " + videoApod.getVideoUrl());
            println("Thumbnail URL: " + videoApod.getThumbnailUrl());
        }
    }

    // Print Rate Limit
    println("Rate Limit: " + api.getRateLimit());
    println("Rate Limit Remaining: " + api.getRateLimitRemaining());
}