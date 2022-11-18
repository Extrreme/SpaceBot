package dev.extrreme.spacebot.base;

import dev.extrreme.spacebot.base.command.CommandManager;
import dev.extrreme.spacebot.base.command.DiscordCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;

public class DiscordBot {
    private final String token;

    private JDA jda = null;
    private CommandManager commandManager = null;

    public DiscordBot(@NotNull String token) throws LoginException {
        this.token = token;
        start();
    }

    private void start() throws LoginException {
        jda = JDABuilder.createDefault(token)
                .build();
        commandManager = new CommandManager(this);
    }

    public void registerCommand(DiscordCommand command) {
        commandManager.registerCommand(command);
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public void registerListener(ListenerAdapter listener) {
        if (jda != null) {
            jda.addEventListener(listener);
        }
    }

    public JDA getJda() {
        return jda;
    }

    public SelfUser getSelf() {
        return jda == null ? null : jda.getSelfUser();
    }
}
