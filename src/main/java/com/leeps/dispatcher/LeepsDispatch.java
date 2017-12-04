package com.leeps.dispatcher;

import io.socket.client.IO;
import io.socket.client.Socket;

import javax.swing.*;
import java.awt.*;
import java.io.UnsupportedEncodingException;

public class LeepsDispatch {
    private static AppFrame appFrame;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowUI();
            }
        });
    }

    private static void createAndShowUI() {
        try {
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            handleSetLookAndFeelException(ex);
        }
        customizeUiComponents();

        appFrame = new AppFrame();
    }

    private static void handleSetLookAndFeelException(Exception ex) {
        System.out.println("app - setLookAndFeel exception - " + ex.getClass().getName());
    }

    private static void customizeUiComponents() {
        Font oldLabelFont = UIManager.getFont("Label.font");
        UIManager.put("Label.font",
                oldLabelFont.deriveFont((float) 11.0).deriveFont(Font.BOLD));

        Font oldButtonFont = UIManager.getFont("Button.font");
        UIManager.put("Button.font",
                oldButtonFont.deriveFont((float) 12.0).deriveFont(Font.BOLD));

        Font oldTextFieldFont = UIManager.getFont("TextField.font");
        UIManager.put("TextField.font",
                oldTextFieldFont.deriveFont((float) 12.0));

    }
}
