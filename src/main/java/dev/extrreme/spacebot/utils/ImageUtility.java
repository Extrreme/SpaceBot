package dev.extrreme.spacebot.utils;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtility {
    public static byte[] getBytes(BufferedImage img) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "jpg", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] getBytes(XYChart chart) {
        return getBytes(BitmapEncoder.getBufferedImage(chart));
    }
}
