package dev.extrreme.spacebot.base.listener;

import dev.extrreme.spacebot.base.DiscordBot;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class BotListener extends ListenerAdapter {
    private final DiscordBot bot;

    public BotListener(DiscordBot bot) {
        this.bot = bot;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        bot.setReady(true);
    }
}
