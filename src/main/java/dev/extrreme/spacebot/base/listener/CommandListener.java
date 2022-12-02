package dev.extrreme.spacebot.base.listener;

import dev.extrreme.spacebot.Main;
import dev.extrreme.spacebot.base.command.CommandManager;
import dev.extrreme.spacebot.base.command.DiscordCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandListener extends ListenerAdapter {
    private final CommandManager manager;

    public CommandListener(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        for (DiscordCommand possibleCommand : manager.getAllCommands(event.getGuild())) {
            if (!possibleCommand.getLabel().equalsIgnoreCase(event.getName())) {
                continue;
            }
            if (!possibleCommand.execute(event)) {
                possibleCommand.sendArgumentsError(event.getTextChannel(), event.getUser());
            }
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        JDA jda = Main.getBot().getJda();

        List<DiscordCommand> commands = manager.getGlobalCommands();
        commands.forEach(command -> jda.upsertCommand(command.toCommandData()).queue());
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        Guild guild = event.getGuild();

        List<DiscordCommand> commands = manager.getGuildCommands(guild);
        commands.forEach(command -> guild.upsertCommand(command.toCommandData()).queue());
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        Guild guild = event.getGuild();

        List<DiscordCommand> commands = manager.getGuildCommands(guild);
        commands.forEach(command -> guild.upsertCommand(command.toCommandData()).queue());
    }
}
