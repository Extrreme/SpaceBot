package dev.extrreme.spacebot.commands;

import dev.extrreme.spacebot.base.command.DiscordCommand;
import dev.extrreme.spacebot.dto.APOD;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class APODCommand extends DiscordCommand {
    public APODCommand() {
        super("apod", "View the Astronomy Picture of the Day");
    }

    @Override
    public boolean execute(SlashCommandEvent event) {
        APOD apod = APOD.getAPOD();

        if (apod != null) {
            event.replyEmbeds(APOD.getAPOD().toEmbed()).queue();
        } else {
            event.reply("Failed to retrieve the Astronomy Picture of the Day, please try again later").queue();
        }

        return true;
    }

    @Override
    public String getCategory() {
        return "Miscellaneous";
    }
}