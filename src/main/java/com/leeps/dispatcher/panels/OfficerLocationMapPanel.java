/**
 * Copyright 2017 LEEPS.Inc
 */
package com.leeps.dispatcher.panels;


import com.leeps.dispatcher.common.*;
import com.leeps.dispatcher.material.MaterialButton;
import com.leeps.dispatcher.material.Roboto;
import com.leeps.dispatcher.service.*;
import de.craften.ui.swingmaterial.MaterialPanel;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import layout.TableLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class OfficerLocationMapPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private AppWideCallsService appWideCallsService;
    private CustomizedUiWidgetsFactory customizedUiWidgetsFactory;

    private JPanel col1Panel;
    private JPanel col2Panel;
    private JLabel officerLocationData;
    private JLabel officerLocationGoto;
    private JLabel officerCrossStreetData;
    private JLabel officerGeoPointData;
    private JLabel officerGeoPointGoto;
    private JLabel officerGeoPointLocation;
    private MaterialButton handledButton;
    private JLabel handledTime;
    private BufferedImage mapGotoImage;
    private BufferedImage mapLocationImage;

    private String developmentTimeMapsApiKey = "AIzaSyBm81MyX0kIFKNnATEWVt5VspajzdVMHYs";
    private BufferedImage currentOfficerGoogleMapsBufferedImage;
    private Image currentOfficerGoogleMapsImage;

    private int currentMapZoomHeightAboveEarth;
    private Browser browser;
    public OfficerLocationMapPanel(AppWideCallsService pAppWideCallsService,
                                   CustomizedUiWidgetsFactory pCustomizedUiWidgetsFactory) {
        appWideCallsService = pAppWideCallsService;
        customizedUiWidgetsFactory = pCustomizedUiWidgetsFactory;
        initImages();

        double officerProfilePanelLayoutSpec[][] = {{
                // columns
                TableLayout.FILL,
                350
        }, {
                // rows
                TableLayout.FILL
        }};

        JPanel hasTwoRowsPanel = new JPanel(new TableLayout(officerProfilePanelLayoutSpec));
        buildRow1Panel();
        buildRow2Panel();
        hasTwoRowsPanel.add(col1Panel, "0, 0");
        hasTwoRowsPanel.add(col2Panel, "1, 0");
        hasTwoRowsPanel.setBorder(BorderFactory.createEmptyBorder());
        setOpaque(true);
        setBorder(BorderFactory.createEmptyBorder());
        setLayout(new GridLayout(0, 1));
        add(hasTwoRowsPanel);
    }

    private void initImages() {
        try {
            InputStream mapGotoStream = getClass()
                    .getResourceAsStream(AppWideStrings.mapGotoImage);
            InputStream mapLocationStream = getClass()
                    .getResourceAsStream(AppWideStrings.mapLocationImage);
            if (mapGotoStream != null) {
                mapGotoImage = ImageIO.read(mapGotoStream);
            }
            if (mapLocationStream != null) {
                mapLocationImage = ImageIO.read(mapLocationStream);
            }
        } catch (IOException ex) {
            System.err.println("app - initImages. Could not read an officer profile picture - "
                    + ex.getMessage());
        }
    }

    private void initFX(JFXPanel fxPanel) {
        // This method is invoked on the JavaFX thread
        Scene scene = createScene();
        fxPanel.setScene(scene);
    }
    private Scene createScene() {
        browser = new Browser();
        Scene scene = new Scene(browser, getWidth(), getHeight(), javafx.scene.paint.Color.web("#666970"));
        URL url = getClass().getResource("../../../../map.html");
        browser.loadURL(url);
        return (scene);
    }
    private void buildRow1Panel() {
        currentMapZoomHeightAboveEarth = 16;

        col1Panel = new JPanel(new GridLayout(0, 1));
        col1Panel.setBackground(AppWideStrings.primaryColor);
        col1Panel.setOpaque(true);
        final JFXPanel fxPanel = new JFXPanel();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(fxPanel);
            }
        });
        col1Panel.add(fxPanel, BorderLayout.CENTER);
    }

    private void buildRow2Panel() {
        double col2PanelLayoutSpec[][] = {{
                // columns
                20,
                40,
                TableLayout.FILL,      // Label
                40,
                10
        }, {
                // rows
                25,
                TableLayout.PREFERRED,      // Location Label
                5,
                50,      // Location Panel
                5,
                TableLayout.PREFERRED,      // Cross Street
                30,
                TableLayout.PREFERRED,      // Geocode point label
                10,
                50,      // Geocode Pane;
                10,
                TableLayout.FILL,
                10,
                42,      // Handle button panel
                10,
                TableLayout.PREFERRED,       //Elapsed Time
                25
        }};

        col2Panel = new JPanel(new TableLayout(col2PanelLayoutSpec));
        col2Panel.setBackground(AppWideStrings.panelBackgroundColor);
        col2Panel.setOpaque(true);

        JLabel officerLocation = new JLabel("OFFICER LOCATION");
        officerLocation.setFont(Roboto.REGULAR.deriveFont(14.0f));
        officerLocation.setForeground(AppWideStrings.primaryColor);

        MaterialPanel locationPanel = new MaterialPanel();
        locationPanel.setLayout(new BorderLayout());
        locationPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 30));
        officerLocationData = new JLabel("LOCATION ADDRESS");
        officerLocationData.setFont(Roboto.BOLD.deriveFont(16.0f));
        officerLocationData.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        officerLocationGoto = new JLabel();
        officerLocationGoto.setIcon(new ImageIcon(mapGotoImage));

        locationPanel.setBackground(Color.WHITE);
        locationPanel.setOpaque(true);
        locationPanel.add(officerLocationGoto, BorderLayout.EAST);
        locationPanel.add(officerLocationData, BorderLayout.CENTER);

        officerCrossStreetData = new JLabel("CROSS STREET");
        officerCrossStreetData.setFont(Roboto.REGULAR.deriveFont(14.0f));
        officerCrossStreetData.setForeground(AppWideStrings.primaryColor);

        JLabel officerGeoPoint = new JLabel("GEOCODE POINT");
        officerGeoPoint.setFont(Roboto.REGULAR.deriveFont(14.0f));
        officerGeoPoint.setForeground(AppWideStrings.primaryColor);

        MaterialPanel geoPointPanel = new MaterialPanel();
        geoPointPanel.setLayout(new BorderLayout());
        geoPointPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 30));
        geoPointPanel.setBackground(AppWideStrings.whiteColor);
        geoPointPanel.setOpaque(true);
        officerGeoPointData = new JLabel("123.456, 456.7890");
        officerGeoPointData.setFont(Roboto.BOLD.deriveFont(16.0f));
        officerGeoPointData.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        officerGeoPointGoto = new JLabel();
        officerGeoPointGoto.setIcon(new ImageIcon(mapGotoImage));
        officerGeoPointLocation = new JLabel();
        officerGeoPointLocation.setIcon(new ImageIcon(mapLocationImage));
        geoPointPanel.add(officerGeoPointData, BorderLayout.CENTER);
        geoPointPanel.add(officerGeoPointLocation, BorderLayout.WEST);
        geoPointPanel.add(officerGeoPointGoto, BorderLayout.EAST);

        handledButton = new MaterialButton("HANDLED", new Color(47, 128, 237), Color.WHITE, new Color(9, 90, 220));
        handledButton.setFont(Roboto.BOLD.deriveFont(20.0f));
        handledButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                       browser.gotoLocation(40.0978f, 124.354865f);
                    }
                });
            }
        });
        handledTime = new JLabel("10:48");
        handledTime.setFont(Roboto.BOLD.deriveFont(16.0f));
        handledTime.setForeground(AppWideStrings.primaryColor);
        handledTime.setHorizontalAlignment(SwingConstants.CENTER);

        col2Panel.add(officerLocation, "1, 1, 3, 1");
        col2Panel.add(locationPanel, "1, 3, 3, 3");
        col2Panel.add(officerCrossStreetData, "1, 5, 3, 5");
        col2Panel.add(officerGeoPoint, "1, 7, 3, 7");
        col2Panel.add(geoPointPanel, "1, 9, 3, 9");
        col2Panel.add(handledButton, "2, 13");
        col2Panel.add(handledTime, "1, 15, 3, 15");
    }

    class Browser extends Region {

        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();

        public Browser() {
            //apply the styles
            getStyleClass().add("browser");
            // load the web page
            webEngine.load("https://www.yahoo.com");
            //add the web view to the scene
            getChildren().add(browser);

        }
        private Node createSpacer() {
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            return spacer;
        }

        public void loadURL(URL url) {
            webEngine.load(url.toExternalForm());
        }

        public void gotoLocation(float lat, float lng) {
            webEngine.executeScript("gotoLocationWithPicker(" + lat + "," + lng + ")");
        }
        @Override protected void layoutChildren() {
            double w = getWidth();
            double h = getHeight();
            layoutInArea(browser,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
        }

        @Override protected double computePrefWidth(double height) {
            return getWidth();
        }

        @Override protected double computePrefHeight(double width) {
            return getHeight();
        }
    }

}
