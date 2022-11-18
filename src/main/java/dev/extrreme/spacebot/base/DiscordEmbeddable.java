package dev.extrreme.spacebot.base;

import net.dv8tion.jda.api.entities.MessageEmbed;

public interface DiscordEmbeddable {
    MessageEmbed toEmbed();
}
