package dev.extrreme.spacebot.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class HTTPClient {

    public static InputStream executeGet(String url, Map<String, String> headers) {
        try {
            return executeGet(new URL(url),headers);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public static InputStream executeGet(URL url, Map<String, String> headers) {
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            headers.forEach(conn::setRequestProperty);
            conn.setRequestMethod("GET");

            conn.connect();

            if (conn.getResponseCode() >= 400) {
                return null;
            }

            return conn.getInputStream();
        } catch (IOException e) {
            return null;
        }
    }
}
