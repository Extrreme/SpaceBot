package dev.extrreme.spacebot.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JSONUtility {
    private static final Gson gson = new Gson();

    public static JsonElement read(InputStream is) {
        if (is == null) {
            return null;
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder jsonString = new StringBuilder();

            String readLine;
            while ((readLine = in.readLine()) != null) {
                jsonString.append(readLine);
            }

            return gson.fromJson(jsonString.toString(), JsonElement.class);
        } catch (IOException e) {
            return null;
        }
    }
}
