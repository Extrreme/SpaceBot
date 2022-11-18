package dev.extrreme.spacebot;

import dev.extrreme.spacebot.base.DiscordBot;
import dev.extrreme.spacebot.commands.APODCommand;
import dev.extrreme.spacebot.commands.HelpCommand;
import dev.extrreme.spacebot.commands.SatelliteInfoCommand;
import dev.extrreme.spacebot.commands.WIISCommand;

import javax.security.auth.login.LoginException;

public class SpaceBot extends DiscordBot {
    public SpaceBot() throws LoginException {
        super(System.getenv("TOKEN"));

        registerListeners();
        registerCommands();
    }

    private void registerListeners() {}

    private void registerCommands() {
        registerCommand(new HelpCommand());
        registerCommand(new SatelliteInfoCommand());
        registerCommand(new APODCommand());
        registerCommand(new WIISCommand());
    }
}
