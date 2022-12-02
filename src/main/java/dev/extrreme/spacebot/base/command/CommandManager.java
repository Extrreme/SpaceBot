package dev.extrreme.spacebot.base.command;

import dev.extrreme.spacebot.base.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CommandManager {
    private final DiscordBot bot;

    private final List<DiscordCommand> globalCommands = new ArrayList<>();
    private final HashMap<Long, List<DiscordCommand>> guildCommands = new HashMap<>();

    public CommandManager(DiscordBot bot) {
        this.bot = bot;
    }

    public void registerGlobalCommand(DiscordCommand command) {
        if (bot.isReady()) {
            bot.getJda().upsertCommand(command.toCommandData()).queue();
        }
        globalCommands.add(command);
    }

    public void registerGuildCommand(DiscordCommand command, Guild... guilds) {
        for (Guild guild : guilds) {
            if (bot.isReady()) {
                guild.upsertCommand(command.toCommandData()).queue();
            }
            guildCommands.computeIfAbsent(guild.getIdLong(), id -> new ArrayList<>()).add(command);
        }
    }

    public void registerGuildCommand(DiscordCommand command, long... guildIds) {
        for (long guildId : guildIds) {
            if (bot.isReady()) {
                Guild guild = bot.getJda().getGuildById(guildId);
                if (guild != null) {
                    guild.upsertCommand(command.toCommandData()).queue();
                }
            }
            guildCommands.computeIfAbsent(guildId, id -> new ArrayList<>()).add(command);
        }
    }

    public List<DiscordCommand> getGlobalCommands() {
        return new ArrayList<>(globalCommands);
    }

    public List<DiscordCommand> getGuildCommands(Guild guild) {
        if (guildCommands.get(guild.getIdLong()) == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(guildCommands.get(guild.getIdLong()));
    }

    public List<DiscordCommand> getAllCommands(Guild guild) {
        List<DiscordCommand> commands = getGlobalCommands();
        commands.addAll(getGuildCommands(guild));

        return commands;
    }

    public Map<String, List<DiscordCommand>> getByCategories(Guild guild) {
        Map<String, List<DiscordCommand>> sorted = new HashMap<>();
        getAllCommands(guild).forEach(command -> {
            String cat = command.getCategory();
            if (!sorted.containsKey(cat)) {
                sorted.put(cat, new ArrayList<>());
            }
            sorted.get(cat).add(command);
        });

        return sorted;
    }
}
