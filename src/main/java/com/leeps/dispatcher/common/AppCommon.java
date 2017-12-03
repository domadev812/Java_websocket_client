package com.leeps.dispatcher.common;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
public class AppCommon {
    public Font getRobotoFont(){
        Font robotoFont = null;
        InputStream imageInputStream1 = getClass().getResourceAsStream(
                AppWideStrings.robotoFontBold);
        try {
            robotoFont = Font.createFont(Font.TRUETYPE_FONT, imageInputStream1);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return robotoFont;
    }

    public Font getRobotoBoldFont(float fontSize){
        Font robotoFont = null;
        InputStream imageInputStream1 = getClass().getResourceAsStream(
                AppWideStrings.robotoFontBold);
        try {
            robotoFont = Font.createFont(Font.TRUETYPE_FONT, imageInputStream1);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return robotoFont.deriveFont(fontSize);
    }

    public Font getRobotoRegularFont(float fontSize){
        Font robotoFont = null;
        InputStream imageInputStream1 = getClass().getResourceAsStream(
                AppWideStrings.robotoFontRegular);
        try {
            robotoFont = Font.createFont(Font.TRUETYPE_FONT, imageInputStream1);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return robotoFont.deriveFont(fontSize);
    }

    public Font getRobotoRegularFontUnderline(float fontSize){
        Font robotoFont = null;
        InputStream imageInputStream1 = getClass().getResourceAsStream(
                AppWideStrings.robotoFontRegular);
        try {
            robotoFont = Font.createFont(Font.TRUETYPE_FONT, imageInputStream1);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        robotoFont = robotoFont.deriveFont(fontSize);
        Map attributes = robotoFont.getAttributes();
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);

        return robotoFont.deriveFont(attributes);
    }

    public Font getRobotoBoldFontUnderline(float fontSize){
        Font robotoFont = null;
        InputStream imageInputStream1 = getClass().getResourceAsStream(
                AppWideStrings.robotoFontBold);
        try {
            robotoFont = Font.createFont(Font.TRUETYPE_FONT, imageInputStream1);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        robotoFont = robotoFont.deriveFont(fontSize);
        Map attributes = robotoFont.getAttributes();
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);

        return robotoFont.deriveFont(attributes);
    }

    public Dimension getScreenSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return screenSize;
    }
}
