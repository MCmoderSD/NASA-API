# [NASA API](https://api.nasa.gov/)

## Description

A lightweight Java wrapper for [NASA's Open APIs](https://api.nasa.gov/). 
It handles HTTP requests, JSON parsing, and rate-limit tracking for you, and exposes typed data models instead of raw JSON.

Currently supported:
- **APOD** (Astronomy Picture of the Day)

## Features

- Simple entry point via `NasaAPI` — one client, multiple sub-APIs (`api.apod()`, and more to come)
- Automatic rate-limit tracking from the `X-RateLimit-*` response headers
- Typed APOD results (`ImageAPOD` / `VideoAPOD`) instead of raw JSON, with convenience getters for images/thumbnails as `BufferedImage`
- Works out of the box with NASA's `DEMO_KEY`, or your own [API key](https://api.nasa.gov/)


## Usage

### Maven
Make sure you have my Sonatype Nexus OSS repository added to your `pom.xml` file:
```xml
<repositories>
    <repository>
        <id>Nexus</id>
        <name>Sonatype Nexus</name>
        <url>https://mcmodersd.de/nexus/repository/maven-releases/</url>
    </repository>
</repositories>
```
Add the dependency to your `pom.xml` file:
```xml
<dependency>
    <groupId>de.MCmoderSD</groupId>
    <artifactId>NASA-API</artifactId>
    <version>1.0.4</version>
</dependency>
```

## Usage Example

### Fetching APOD (Astronomy Picture of the Day)
```java
import de.MCmoderSD.nasa.api.apod.data.ImageAPOD;
import de.MCmoderSD.nasa.api.apod.data.VideoAPOD;
import de.MCmoderSD.nasa.core.NasaAPI;

import static java.lang.IO.println;

void main() {

    // API Key
    var apiKey = "DEMO_KEY"; // Replace it with your own API key from https://api.nasa.gov/

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
```