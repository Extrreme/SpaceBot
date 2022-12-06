package dev.extrreme.spacebot;

import dev.extrreme.spacebot.base.DiscordBot;
import dev.extrreme.spacebot.base.command.CommandManager;
import dev.extrreme.spacebot.commands.APODCommand;
import dev.extrreme.spacebot.commands.HelpCommand;
import dev.extrreme.spacebot.commands.SatelliteInfoCommand;
import dev.extrreme.spacebot.commands.WIISCommand;
import dev.extrreme.spacebot.events.ButtonListener;

import javax.security.auth.login.LoginException;

public class SpaceBot extends DiscordBot {
    public SpaceBot() throws LoginException {
        super(System.getenv("TOKEN"));

        registerListeners();
        registerCommands();
    }

    private void registerListeners() {
        registerListener(new ButtonListener());
    }

    private void registerCommands() {
        CommandManager commandManager = getCommandManager();

        commandManager.registerGlobalCommand(new HelpCommand());
        commandManager.registerGlobalCommand(new SatelliteInfoCommand());
        commandManager.registerGlobalCommand(new APODCommand());
        commandManager.registerGlobalCommand(new WIISCommand());
    }
}
