package dev.extrreme.spacebot.base.command;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class CommandListener extends ListenerAdapter {
    private final CommandManager manager;

    public CommandListener(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        User author = event.getAuthor();
        String message = event.getMessage().getContentRaw();

        if (author.isBot() || !message.startsWith(manager.getPrefix())) {
            return;
        }

        message = message.substring(manager.getPrefix().length());
        String[] split = message.split(" ");

        String command = split[0];
        String[] args = Arrays.copyOfRange(split, 1, split.length);

        manager.onCommand(event.getGuild(), event.getTextChannel(), author, command, args);
    }
}
