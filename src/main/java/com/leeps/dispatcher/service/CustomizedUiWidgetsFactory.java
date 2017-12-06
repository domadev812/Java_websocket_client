/**
 * Copyright 2017 LEEPS.Inc
 */
package com.leeps.dispatcher.service;

import com.leeps.dispatcher.common.AppWideStrings;
import de.craften.ui.swingmaterial.fonts.Roboto;
import layout.TableLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;

public class CustomizedUiWidgetsFactory {

    public CustomizedUiWidgetsFactory() {
    }

    public JButton makeClickableTextLink(String pText) {
        return makeClickableTextLink(pText, Color.BLUE, null);
    }

    public JButton makeClickableTextLink(String pText, Color pNonFocusForegroundColor,
                                         Color pBackgroundColor) {

        final JButton aJButton = new JButton();
        aJButton.setForeground(pNonFocusForegroundColor);

        if (pBackgroundColor != null) {
            aJButton.setBackground(pBackgroundColor);
        }

        final String notHoveredText = "<html><strong>" + pText
                + "</strong></html>";

        final String hoveredText = "<html><strong><u>" + pText
                + "</u></strong></html>";

        final String mousePressedText = "<html><strong><font color=\"#FF0000\"><u>" + pText
                + "</u></font></strong></html>";

        aJButton.setText(notHoveredText);
        aJButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        aJButton.setHorizontalAlignment(SwingConstants.LEFT);
        aJButton.setMargin(new Insets(3, 3, 3, 3));
        aJButton.setContentAreaFilled(false);
        aJButton.setFocusPainted(true);
        aJButton.setBorderPainted(false);
        aJButton.setOpaque(false);
        aJButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent pE) {
                aJButton.setText(hoveredText);
            }
            @Override
            public void mouseExited(MouseEvent pE) {
                aJButton.setText(notHoveredText);
            }
            @Override
            public void mousePressed(MouseEvent pE) {
                aJButton.setText(mousePressedText);
            }
            @Override
            public void mouseReleased(MouseEvent pE) {
                aJButton.setText(notHoveredText);
            }
        });
        return aJButton;
    }

    public JButton makeNonSeeableButton() {
        JButton returnButton = new JButton();
        returnButton.setPreferredSize(new Dimension(1, 1));
        returnButton.setEnabled(false);
        returnButton.setFocusable(false);
        return returnButton;
    }

    public JLabel makeFam3IconLabel(
            String pIconFilename, String pToolTipText, int pWidth, int pHeight) {
        JLabel returnLabel = new JLabel(makeImageIcon(pIconFilename));
        returnLabel.setPreferredSize(new Dimension(pWidth, pHeight));
        returnLabel.setToolTipText(pToolTipText);
        return returnLabel;
    }

    public JButton makeFam3IconButton(
            String pIconFilename, String pToolTipText, int pWidth, int pHeight) {
        JButton returnButton = new JButton();
        returnButton.setIcon(makeImageIcon(pIconFilename));
        returnButton.setPreferredSize(new Dimension(pWidth, pHeight));
        returnButton.setToolTipText(pToolTipText);
        return returnButton;
    }

    public ImageIcon makeImageIcon(String pIconFilename) {
        return makeImageIcon(pIconFilename, AppWideStrings.appFam3IconsLoc);
    }

    public ImageIcon makeImageIcon(String pIconFilename, String pDirectoryLoc) {
        BufferedImage iconBufferedImage = null;
        ImageIcon returnImageIcon = null;

        try {
            InputStream iconInputStream = getClass().getResourceAsStream(
                    pDirectoryLoc + pIconFilename);

            if (iconInputStream != null) {
                iconBufferedImage = ImageIO.read(iconInputStream);
                returnImageIcon = new ImageIcon(iconBufferedImage);
            }

        } catch (IOException ex) {
            System.err.println("app - makeImageIcon. Could not read icon - "
                    + pIconFilename + ", " + ex.getMessage());
        }
        return returnImageIcon;
    }

    public JPanel makeIndentedJPanel(int pLeftIndentPixels, JComponent pJComponent) {
        JPanel returnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        if (pLeftIndentPixels > 0) {
            returnPanel.add(Box.createHorizontalStrut(pLeftIndentPixels));
        }
        if (pJComponent != null) {
            returnPanel.add(pJComponent);
        }
        returnPanel.setBorder(BorderFactory.createEmptyBorder());
        returnPanel.setBackground(AppWideStrings.panelBackgroundColor);
        return returnPanel;
    }

    public JPanel applyFontSizeAlignBgColorToLabel(
            float pFontSize, JLabel pLabel, int pAlign, Color pBackgroundColor) {
        double tableLayoutSpec[][] = new double[][] { {
                TableLayout.PREFERRED, TableLayout.FILL,
                TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED,
        }, {
                TableLayout.FILL
        } };
        pLabel.setFont(Roboto.REGULAR.deriveFont(pFontSize));
        JPanel labelInPanel = new JPanel(new GridLayout(0, 1));
        labelInPanel.add(pLabel);

        JPanel returnPanel = new JPanel(new TableLayout(tableLayoutSpec));
        if (pAlign == SwingConstants.LEFT) {
            returnPanel.add(labelInPanel, "0,0");
        } else if (pAlign == SwingConstants.CENTER) {
            returnPanel.add(labelInPanel, "2,0");
        } else {
            returnPanel.add(labelInPanel, "4,0");
        }

        if (pBackgroundColor != null) {
            labelInPanel.setBackground(pBackgroundColor);
        }
        returnPanel.setBackground(AppWideStrings.panelBackgroundColor);
        return returnPanel;
    }

    public JTextArea makeWordWrapTextArea(int pRows, int pColumns,
                                          String pTextString, int fontSize) {

        JTextArea returnTextArea = new JTextArea(pRows, pColumns);
        returnTextArea.setFont(new Font("Arial", Font.BOLD, fontSize));
        returnTextArea.setLineWrap(true);
        returnTextArea.setWrapStyleWord(true);
        returnTextArea.setText(pTextString);

        return returnTextArea;
    }

    public void makeTextAreaReadOnlyInfo(JTextArea pTextArea) {
        pTextArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pTextArea.setEditable(false);
        pTextArea.setFocusable(false);
//        pTextArea.setBackground(AppWideStrings.innerPanelBackgroundColor);
    }

    public static class AutoHeightAdjustTextArea extends JTextArea {
        private static final long serialVersionUID = 1L;

        public AutoHeightAdjustTextArea() {
            this(0, 0, "", 12);
        }

        public AutoHeightAdjustTextArea(
                int pRows, int pColumns, String pTextString, int fontSize) {

            super(pRows, pColumns);
            setFont(new Font("Arial", Font.BOLD, fontSize));
            setLineWrap(true);
            setWrapStyleWord(true);
            setText(pTextString);
        }
    }

    public String readTextFileIntoString(String pDirPathAndFileName) {
        File readFile;
        FileInputStream aFileInputStream;
        ByteArrayOutputStream aByteArrayOutputStream;

        readFile = new File(pDirPathAndFileName);

        try {
            aFileInputStream = new FileInputStream(readFile);
            aByteArrayOutputStream = new ByteArrayOutputStream(4000);

            byte[] byteArray = new byte[4000];
            int readLength;

            while ((readLength = aFileInputStream.read(byteArray)) != -1) {
                aByteArrayOutputStream.write(byteArray, 0, readLength);
            }

            aFileInputStream.close();
            aByteArrayOutputStream.close();

            return aByteArrayOutputStream.toString();

        } catch (IOException ex) {
            System.err.println(
                    "app - readTextFileIntoString() can't read file: " + pDirPathAndFileName);
            ex.printStackTrace();
            return "";
        }
    }

    public void removeAllMouseListeners(JComponent pJComponent) {
        MouseListener[] mmListeners = pJComponent.getMouseListeners();
        if ((mmListeners != null) && (mmListeners.length > 0)) {
            for (MouseListener eachMml : mmListeners) {
                pJComponent.removeMouseListener(eachMml);
            }
        }
    }

    public void makeComponentRedoSeeThrough(JComponent pJComponent) {
        int currentWidth = pJComponent.getWidth();
        pJComponent.setSize(currentWidth + 1, pJComponent.getHeight());
        pJComponent.setSize(currentWidth, pJComponent.getHeight());
    }
}
