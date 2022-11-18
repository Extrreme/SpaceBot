package dev.extrreme.spacebot.commands;

import dev.extrreme.spacebot.base.command.DiscordCommand;
import dev.extrreme.spacebot.dto.Satellite;
import dev.extrreme.spacebot.utils.NumberUtility;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class SatelliteInfoCommand extends DiscordCommand {
    public SatelliteInfoCommand() {
        super("satinfo", "View information about a satellite");
    }

    @Override
    public boolean execute(Guild guild, TextChannel channel, User sender, String... args) {
        if (args.length < 1 || !NumberUtility.isInteger(args[0])) {
            channel.sendMessage(sender.getAsMention() + "\nPlease specify a valid, numeric, NORAD catalog number").queue();
            return false;
        }

        Satellite sat = Satellite.getData(Integer.parseInt(args[0]));

        if (sat == null) {
            channel.sendMessage(sender.getAsMention() + "\nNo satellite with id: " + args[0] + " could be found").queue();
            return true;
        }

        channel.sendMessageEmbeds(sat.toEmbed()).queue();
        return true;
    }

    @Override
    public String getCategory() {
        return "Satellite";
    }
}
