package dev.extrreme.spacebot;

import javax.security.auth.login.LoginException;

public class Main {
    private static SpaceBot bot;

    public static void main(String[] args) throws LoginException {
        bot = new SpaceBot();
    }

    public static SpaceBot getBot() {
        return bot;
    }
}
