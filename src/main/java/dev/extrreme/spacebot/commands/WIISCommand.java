package dev.extrreme.spacebot.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.extrreme.spacebot.base.command.DiscordCommand;
import dev.extrreme.spacebot.dto.APOD;
import dev.extrreme.spacebot.utils.HTTPClient;
import dev.extrreme.spacebot.utils.JSONUtility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

public class WIISCommand extends DiscordCommand {
    public WIISCommand() {
        super("who-is-in-space", "See who is in space");
    }

    @Override
    public boolean execute(Guild guild, TextChannel channel, User sender, String... args) {
        channel.sendMessageEmbeds(getPeople()).queue();
        return true;
    }

    @Override
    public String getCategory() {
        return "Miscellaneous";
    }

    public MessageEmbed getPeople() {
        String endpoint = "http://api.open-notify.org/astros.json";

        StringBuilder sb = new StringBuilder();

        try (InputStream is = Objects.requireNonNull(HTTPClient.executeGet(endpoint, new HashMap<>()))) {
            JsonArray json = JSONUtility.read(is).getAsJsonObject().get("people").getAsJsonArray();

            for (JsonElement jsonP : json) {
                String name = jsonP.getAsJsonObject().get("name").getAsString();
                String craft = jsonP.getAsJsonObject().get("craft").getAsString();

                sb.append("**").append(name).append("** - *").append(craft).append("*\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new EmbedBuilder().setColor(Color.ORANGE)
                .setTitle("Who Is In Space?")
                .setDescription(sb)
                .setThumbnail("https://t4.ftcdn.net/jpg/02/80/96/19/360_F_280961966_KlMAkHZvzu0sTM0MWtYSb69PmYFdQhba.jpg")
                .build();
    }
}
