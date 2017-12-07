/**
 * Copyright 2017 LEEPS.Inc
 */
package com.leeps.dispatcher.panels;


import com.leeps.dispatcher.common.*;
import com.leeps.dispatcher.material.MaterialButton;
import com.leeps.dispatcher.material.MultiLineLabel;
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
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.json.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

public class OfficerLocationMapPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private AppWideCallsService appWideCallsService;
    private CustomizedUiWidgetsFactory customizedUiWidgetsFactory;
    private String developmentTimeMapsApiKey = "AIzaSyBm81MyX0kIFKNnATEWVt5VspajzdVMHYs";

    private JPanel col1Panel;
    private JPanel col2Panel;
    private JTextArea officerLocationData;
    private JLabel officerLocationGoto;
    private JTextArea officerCrossStreetData;
    private JLabel officerGeoPointData;
    private JLabel officerGeoPointGoto;
    private JLabel officerGeoPointLocation;
    private MaterialButton handledButton;
    private JLabel officerHandledTime;
    private BufferedImage mapGotoImage;
    private BufferedImage mapLocationImage;
    private float lat, lon;
    private Browser browser;

    boolean threadFlag = false;
    int durationSeconds = 0;

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

        initLocationData();
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
                TableLayout.PREFERRED,      // Location Panel
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
        locationPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 30));
        officerLocationData = new JTextArea("");
        officerLocationData.setFont(Roboto.BOLD.deriveFont(16.0f));
        officerLocationData.setForeground(AppWideStrings.primaryColor);
        officerLocationData.setLineWrap(true);
        officerLocationData.setEditable(false);
        officerLocationData.setBackground(AppWideStrings.whiteColor);
        officerLocationData.setOpaque(true);
        officerLocationData.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        officerLocationGoto = new JLabel();
        officerLocationGoto.setIcon(new ImageIcon(mapGotoImage));

        locationPanel.setBackground(Color.WHITE);
        locationPanel.setOpaque(true);
//        locationPanel.add(officerLocationGoto, BorderLayout.EAST);
        locationPanel.add(officerLocationData, BorderLayout.CENTER);

        officerCrossStreetData = new JTextArea(AppWideStrings.crossStreet);
        officerCrossStreetData.setFont(Roboto.REGULAR.deriveFont(14.0f));
        officerCrossStreetData.setForeground(AppWideStrings.primaryColor);
        officerCrossStreetData.setLineWrap(true);
        officerCrossStreetData.setEditable(false);
        officerCrossStreetData.setBackground(AppWideStrings.panelBackgroundColor);
        officerCrossStreetData.setOpaque(true);
        officerCrossStreetData.setBorder(BorderFactory.createEmptyBorder());

        JLabel officerGeoPoint = new JLabel("GEOCODE POINT");
        officerGeoPoint.setFont(Roboto.REGULAR.deriveFont(14.0f));
        officerGeoPoint.setForeground(AppWideStrings.primaryColor);

        MaterialPanel geoPointPanel = new MaterialPanel();
        geoPointPanel.setLayout(new BorderLayout());
        geoPointPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 30));
        geoPointPanel.setBackground(AppWideStrings.whiteColor);
        geoPointPanel.setOpaque(true);

        officerGeoPointData = new JLabel("");
        officerGeoPointData.setFont(Roboto.BOLD.deriveFont(16.0f));
        officerGeoPointData.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        officerGeoPointGoto = new JLabel();
        officerGeoPointGoto.setIcon(new ImageIcon(mapGotoImage));
        officerGeoPointLocation = new JLabel();
        officerGeoPointLocation.setIcon(new ImageIcon(mapLocationImage));

        geoPointPanel.add(officerGeoPointData, BorderLayout.CENTER);
        geoPointPanel.add(officerGeoPointLocation, BorderLayout.WEST);
//        geoPointPanel.add(officerGeoPointGoto, BorderLayout.EAST);

        handledButton = new MaterialButton("HANDLED", new Color(47, 128, 237), Color.WHITE, new Color(9, 90, 220));
        handledButton.setFont(Roboto.BOLD.deriveFont(20.0f));
        handledButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handledOfficer();
            }
        });
        officerHandledTime = new JLabel("10:48");
        officerHandledTime.setFont(Roboto.BOLD.deriveFont(16.0f));
        officerHandledTime.setForeground(AppWideStrings.primaryColor);
        officerHandledTime.setHorizontalAlignment(SwingConstants.CENTER);

        col2Panel.add(officerLocation, "1, 1, 3, 1");
        col2Panel.add(locationPanel, "1, 3, 3, 3");
        col2Panel.add(officerCrossStreetData, "1, 5, 3, 5");
        col2Panel.add(officerGeoPoint, "1, 7, 3, 7");
        col2Panel.add(geoPointPanel, "1, 9, 3, 9");
        col2Panel.add(handledButton, "2, 13");
        col2Panel.add(officerHandledTime, "1, 15, 3, 15");
    }

    private void handledOfficer() {
        appWideCallsService.setHandled(false);
        JSONObject handledOfficer = appWideCallsService.getHandledOfficer();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KeyStrings.keyAction, KeyStrings.keyCurrentOfficerHandled);
            jsonObject.put(KeyStrings.keyOfficerID, handledOfficer.getInt(KeyStrings.keyID));
            appWideCallsService.sendToServer(jsonObject);
            appWideCallsService.setHandledOfficer(null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        appWideCallsService.showOfficerProfileUiDataModel(null);
        initLocationData();
        appWideCallsService.initGraphData(null);
    }

    public void initLocationData(){
        durationSeconds = 0;
        threadFlag = false;
        handledButton.setConfiguration(new Color(0x4F, 0x4F, 0x4F), Color.WHITE, new Color(0x4F, 0x4F, 0x4F));
        handledButton.setEnabled(false);
        officerCrossStreetData.setText(AppWideStrings.crossStreet);
        officerLocationData.setText("");
        officerGeoPointData.setText("");
        officerHandledTime.setText("");
    }

    public void initLocation(boolean flag) {
        lat = (float) appWideCallsService.getLat();
        lon = (float) appWideCallsService.getLon();
        if(flag) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    browser.gotoLocation(lat, lon);
                }
            });
            handledButton.setConfiguration(new Color(41, 117, 234), Color.WHITE, new Color(30, 110, 230));
            handledButton.setEnabled(true);
            threadFlag = true;
            TimerThread thread = new TimerThread();
            thread.start();
        }
        reverseGeocodeCurrentOfficerLocFromXY(lat + ", " + lon);
    }

    public void showDurationTime() {
        String durationTime = "";
        int mins = durationSeconds / 60;
        int secs = durationSeconds % 60;
        if(mins < 10) durationTime += "0" + mins;
        else durationTime += mins;
        durationTime += ":";
        if(secs < 10) durationTime += "0" + secs;
        else durationTime += secs;

        officerHandledTime.setText(durationTime);
    }
    private double truncateDoubleToNdecimalPlaces(
            String pInputDoubleString, int pKeepNdecimalPlaces) {

        double powerOf10 = Math.pow(10, pKeepNdecimalPlaces);
        double originalDouble = Double.parseDouble(pInputDoubleString);
        long turnIntoLong = Math.round(originalDouble * powerOf10);
        double returnDouble = turnIntoLong / powerOf10;
        return returnDouble;
    }

    private void reverseGeocodeCurrentOfficerLocFromXY(String pXAndYString) {
        String mapThisGeoCodeString = null;
        if(!appWideCallsService.isHandled()) return;
        mapThisGeoCodeString = pXAndYString.trim().replaceAll(" ", "");

        String[] xOrYGeoCodeStringArray = mapThisGeoCodeString.split(",");

        double mapCenterX = 0;
        double mapCenterY = 0;
        mapCenterX = truncateDoubleToNdecimalPlaces(xOrYGeoCodeStringArray[0], 6);
        mapCenterY = truncateDoubleToNdecimalPlaces(xOrYGeoCodeStringArray[1], 6);

        ByteArrayOutputStream aOutputStream = null;
        try {
            String requestSpecString = AppWideStrings.googleMapsRequestCrossStreetsUrlString
                    + mapThisGeoCodeString
                    + "&key="
                    + developmentTimeMapsApiKey;

            URL mapUrl = new URL(requestSpecString);
            InputStream aInputStream = mapUrl.openStream();
            aOutputStream = new ByteArrayOutputStream(2048);

            byte[] byteArray = new byte[2048];
            int readLength = 0;

            while ((readLength = aInputStream.read(byteArray)) != -1) {
                aOutputStream.write(byteArray, 0, readLength);
            }

            aInputStream.close();
            aOutputStream.flush();
            aOutputStream.close();

        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }

        JsonReader aJsonReader = Json.createReader(
                new ByteArrayInputStream(aOutputStream.toByteArray()));

        JsonObject aJsonObjectOuter = aJsonReader.readObject();
        if (!(aJsonObjectOuter.getString("status").equalsIgnoreCase("OK"))) {
//            geoCodeTextField.setBackground(Color.RED);
            return;
        }

        JsonArray aJsonArrayResults = aJsonObjectOuter.getJsonArray("results");

        if (aJsonArrayResults == null) {
//            geoCodeTextField.setBackground(Color.RED);
            return;
        }

        Iterator<JsonValue> aIterator = aJsonArrayResults.iterator();

        int index = 0;
        String fullAddressString = "";
        String crossStreetAddressString = "";
        String thirdLineAddressString = "";

        while (aIterator.hasNext()) {
            JsonValue aJsonValueAddresses = aIterator.next();
            if (aJsonValueAddresses.getValueType() == JsonValue.ValueType.OBJECT) {
                JsonObject aJsonAddresesObject = (JsonObject) aJsonValueAddresses;
                String addressString = aJsonAddresesObject.getString("formatted_address");
                if ((addressString != null) && (addressString.length() > 0)) {
                    if (index == 0) {
                        fullAddressString = addressString;
                        System.out.println("Full Address is " + fullAddressString);

                    } else if (index == 1) {
                        crossStreetAddressString = addressString;
                        System.out.println("Cross Street Address is " + crossStreetAddressString);

                    } else if (index == 2) {
                        thirdLineAddressString = addressString;
                        if (addressString.contains("/")) {
                            System.out.println("Cross Street Address is " + thirdLineAddressString);
                        } else {
                            System.out.println("3rd Address is " + thirdLineAddressString);
                        }

                    } else if (addressString.contains("/")) {
                        crossStreetAddressString = addressString;
                        System.out.println("Cross Street Address is " + crossStreetAddressString);
                    } else {
                        System.out.println("Generalized Address is " + addressString);
                    }
                }
            }
            index++;
        }

        officerLocationData.setText(fullAddressString);
        officerCrossStreetData.setText(AppWideStrings.crossStreet + " " + crossStreetAddressString);
        officerGeoPointData.setText(mapCenterX + "," + mapCenterY);
    }

    public class TimerThread extends Thread {
        public void run(){
            while(threadFlag) {
                durationSeconds++;
                showDurationTime();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
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
