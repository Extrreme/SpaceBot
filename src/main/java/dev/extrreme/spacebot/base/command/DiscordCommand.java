package dev.extrreme.spacebot.base.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public abstract class DiscordCommand {
    private final String label;
    private final String description;

    public DiscordCommand(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public String getLabel() {
        return this.label;
    }

    public String getDescription() {
        return this.description;
    }

    public void sendArgumentsError(TextChannel channel, User user) {
        channel.sendMessage(user.getAsMention() + "\nError, please check your command arguments and try again.").queue();
    }

    public abstract boolean execute(Guild guild, TextChannel channel, User sender, String... args);

    public abstract String getCategory();
}
