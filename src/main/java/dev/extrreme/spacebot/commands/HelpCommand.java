package dev.extrreme.spacebot.commands;

import dev.extrreme.spacebot.Main;
import dev.extrreme.spacebot.base.command.DiscordCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.List;
import java.util.Map;

public class HelpCommand extends DiscordCommand {
    public HelpCommand() {
        super("help", "List all the commands.");
    }

    @Override
    public boolean execute(SlashCommandEvent event) {
        Map<String, List<DiscordCommand>> categorized = Main.getBot().getCommandManager()
                .getByCategories(event.getGuild());

        StringBuilder sb = new StringBuilder("__**Commands:**__\n\n");
        for (String category : categorized.keySet()) {
            sb.append("__").append(category).append(":").append("__\n");
            List<DiscordCommand> commands = categorized.get(category);
            for (DiscordCommand command : commands) {
                if (command == this) {
                    continue;
                }
                sb.append("/").append(command.getLabel()).append(": ").append(command.getDescription()).append("\n");
            }
            sb.append("\n");
        }

        event.reply(event.getUser().getAsMention() + "\n" + sb).queue();
        return true;
    }

    @Override
    public String getCategory() {
        return "Miscellaneous";
    }
}
