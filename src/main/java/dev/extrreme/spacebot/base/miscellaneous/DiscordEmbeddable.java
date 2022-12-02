package dev.extrreme.spacebot.base.miscellaneous;

import net.dv8tion.jda.api.entities.MessageEmbed;

public interface DiscordEmbeddable {
    MessageEmbed toEmbed();
}
