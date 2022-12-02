package dev.extrreme.spacebot.base.command;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public abstract class DiscordCommand {
    private final String label;
    private final String description;
    private final OptionData[] options;

    public DiscordCommand(String label, String description, OptionData... options) {
        this.label = label;
        this.description = description;
        this.options = options;
    }

    public String getLabel() {
        return this.label;
    }

    public String getDescription() {
        return this.description;
    }

    public List<OptionData> getOptions() {
        return List.of(options);
    }

    public void sendArgumentsError(TextChannel channel, User user) {
        channel.sendMessage(user.getAsMention() + "\nError, please check your command arguments and try again.").queue();
    }

    public abstract boolean execute(SlashCommandEvent event);

    public abstract String getCategory();

    public CommandData toCommandData() {
        return new CommandData(label, description).addOptions(options);
    }
}
