package dev.extrreme.spacebot.commands;

import dev.extrreme.spacebot.Main;
import dev.extrreme.spacebot.base.command.DiscordCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;
import java.util.Map;

public class HelpCommand extends DiscordCommand {
    public HelpCommand() {
        super("help", "List all the commands.");
    }

    @Override
    public boolean execute(Guild guild, TextChannel channel, User sender, String... args) {
        String prefix = Main.getBot().getCommandManager().getPrefix();
        Map<String, List<DiscordCommand>> categorized = Main.getBot().getCommandManager().getByCategories();

        StringBuilder sb = new StringBuilder("__**Commands:**__\n\n");
        for (String category : categorized.keySet()) {
            sb.append("__").append(category).append(":").append("__\n");
            List<DiscordCommand> commands = categorized.get(category);
            for (DiscordCommand command : commands) {
                if (command == this) {
                    continue;
                }
                sb.append(prefix).append(command.getLabel()).append(": ").append(command.getDescription()).append("\n");
            }
            sb.append("\n");
        }

        channel.sendMessage(sender.getAsMention() + "\n" + sb).queue();
        return true;
    }

    @Override
    public String getCategory() {
        return "Miscellaneous";
    }
}
