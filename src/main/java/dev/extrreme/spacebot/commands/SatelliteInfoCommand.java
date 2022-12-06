package dev.extrreme.spacebot.commands;

import dev.extrreme.spacebot.base.command.DiscordCommand;
import dev.extrreme.spacebot.dto.Satellite;
import dev.extrreme.spacebot.events.ButtonListener;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.UUID;

public class SatelliteInfoCommand extends DiscordCommand {
    private static final String SATELLITE_OPTION = "satellite-id";
    private static final OptionData SATELLITE_OPTION_DATA = new OptionData(OptionType.INTEGER, SATELLITE_OPTION,
            "The NORAD catalog id of the satellite", true);

    public SatelliteInfoCommand() {
        super("satinfo", "View information about a satellite", SATELLITE_OPTION_DATA);
    }

    @Override
    public boolean execute(SlashCommandEvent event) {
        OptionMapping satelliteOption = event.getOption(SATELLITE_OPTION);
        if (satelliteOption == null) {
            event.reply(event.getUser().getAsMention() + "\nPlease specify a valid, numeric, NORAD catalog number")
                    .queue();
            return true;
        }

        int satelliteId = (int) satelliteOption.getAsLong();
        Satellite sat = Satellite.getData(satelliteId);

        if (sat == null) {
            event.reply(event.getUser().getAsMention() + "\nNo satellite with id: " + satelliteId + " could be found")
                    .queue();
            return true;
        }

        UUID randomId = UUID.randomUUID();

        Message mb = new MessageBuilder()
                .setEmbeds(sat.toEmbed())
                .setActionRows(ActionRow.of(Button.success(randomId.toString(), "Simulate Orbit")))
                .build();

        ButtonListener.satButtonCache.put(randomId, sat);

        event.reply(mb).queue();
        return true;
    }

    @Override
    public String getCategory() {
        return "Satellite";
    }
}
