package com.leeps.dispatcher.material;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class Roboto {
    public static final Font BLACK = loadFont("Roboto-Black.ttf").deriveFont(Font.BOLD);
    public static final Font BOLD = loadFont("Roboto-Bold.ttf").deriveFont(Font.BOLD);
    public static final Font REGULAR = loadFont("Roboto-Regular.ttf").deriveFont(Font.PLAIN);
    private static Font loadFont(String resourceName) {
        try (InputStream inputStream = Roboto.class.getResourceAsStream("../../../../" + resourceName)) {
            return Font.createFont(Font.TRUETYPE_FONT, inputStream);
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException("Could not load " + resourceName, e);
        }
    }
}