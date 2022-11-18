package dev.extrreme.spacebot.commands;

import dev.extrreme.spacebot.base.command.DiscordCommand;
import dev.extrreme.spacebot.dto.APOD;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class APODCommand extends DiscordCommand {
    public APODCommand() {
        super("apod", "View the Astronomy Picture of the Day");
    }

    @Override
    public boolean execute(Guild guild, TextChannel channel, User sender, String... args) {
        APOD apod = APOD.getAPOD();

        if (apod != null) {
            channel.sendMessageEmbeds(APOD.getAPOD().toEmbed()).queue();
        } else {
            channel.sendMessage("Failed to retrieve the Astronomy Picture of the Day, please try again later").queue();
        }

        return true;
    }

    @Override
    public String getCategory() {
        return "Miscellaneous";
    }
}