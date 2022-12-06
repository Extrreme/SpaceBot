package dev.extrreme.spacebot.events;

import dev.extrreme.spacebot.dto.Satellite;
import dev.extrreme.spacebot.simulation.OrbitSimulation;
import dev.extrreme.spacebot.utils.ImageUtility;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.knowm.xchart.XYChart;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ButtonListener extends ListenerAdapter {
    public static final Map<UUID, Satellite> satButtonCache = new HashMap<>();

    public void onButtonClick(ButtonClickEvent event) {
        String id = event.getComponentId();

        if (!isUUID(id)) {
            return;
        }

        Satellite sat = satButtonCache.get(UUID.fromString(id));

        XYChart perifocalOrbit = new OrbitSimulation(sat, 1, 10000).simulate().getPerifocalPlot();

        event.getChannel().sendFile(ImageUtility.getBytes(perifocalOrbit), sat.getName().replace(" ", "_") + "_sim.jpg")
                .queue();
        event.editButton(null).queue();
    }

    @SuppressWarnings("all")
    private static boolean isUUID(String str) {
        try {
            UUID.fromString(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}