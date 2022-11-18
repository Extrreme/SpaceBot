package dev.extrreme.spacebot.dto;

import com.google.gson.JsonObject;
import dev.extrreme.spacebot.base.DiscordEmbeddable;
import dev.extrreme.spacebot.utils.HTTPClient;
import dev.extrreme.spacebot.utils.JSONUtility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Objects;

// Astronomy Picture of the Day
public class APOD implements DiscordEmbeddable {
    private static APOD apod = null;

    private final String photographer;
    private final LocalDate date;

    private final String title;
    private final String description;

    private final String url;

    public APOD(String photographer, LocalDate date, String title, String description, String url) {
        this.photographer = photographer;
        this.date = date;
        this.title = title;
        this.description = description;
        this.url = url;
    }

    public String getPhotographer() {
        return photographer;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public MessageEmbed toEmbed() {
        return new EmbedBuilder().setColor(Color.ORANGE)
                .setTitle("__" + date.toString() + " / " + title + "__")
                .setDescription(description)
                .setImage(url)
                .setFooter("Credit: " + photographer)
                .build();
    }

    public static APOD getAPOD() {
        if (apod == null || !apod.date.equals(LocalDate.now())) {
            String endpoint = "https://api.nasa.gov/planetary/apod?api_key=" + System.getenv("NASA_API_KEY");

            try (InputStream is = Objects.requireNonNull(HTTPClient.executeGet(endpoint, new HashMap<>()))) {
                JsonObject json = JSONUtility.read(is).getAsJsonObject();

                if (json == null) {
                    return null;
                }

                String photographer;
                if (json.get("copyright") != null) {
                    photographer = json.get("copyright").getAsString();
                } else {
                    photographer = "NASA";
                }

                LocalDate date = LocalDate.parse(json.get("date").getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);

                String title = json.get("title").getAsString();
                String description = json.get("explanation").getAsString();

                String url = json.get("hdurl").getAsString();

                apod = new APOD(photographer, date, title, description, url);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return apod;
    }
}
