/**
 * Copyright 2017 LEEPS.Inc
 */
package com.leeps.dispatcher.panels;


import com.leeps.dispatcher.common.*;
import com.leeps.dispatcher.service.*;
import layout.TableLayout;

import javax.imageio.ImageIO;
import javax.json.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

public class OfficerLocationMapPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private AppWideCallsService appWideCallsService;
    private CustomizedUiWidgetsFactory customizedUiWidgetsFactory;
    private JPanel currentOfficerLocationMapPicPanel;
    private JPanel isOnRightOfMapImagePanel;
    private JLabel currentOfficerLocationMapPicLabel;
    private String developmentTimeMapsApiKey = "AIzaSyBm81MyX0kIFKNnATEWVt5VspajzdVMHYs";
    private BufferedImage currentOfficerGoogleMapsBufferedImage;
    private Image currentOfficerGoogleMapsImage;
    private String fullAddressString;
    private String crossStreetAddressString;
    private String thirdLineAddressString;
    private JTextArea fullAddressTextArea;
    private JTextArea crossStreetAddressTextArea;
    private JTextArea thirdLineAddressTextArea;
    private JTextField geoCodeTextField;
    private JButton mapAddressMapAboveAddressButton;
    private JButton mapAddressCenterOnOfficerButton;
    private JButton mapAddressMapAboveGeocodeButton;
    private JLabel mapAddressMapZoomLevelLabel;
    private JButton mapAddressMapZoomInButton;
    private JButton mapAddressMapZoomOutButton;
    private JButton mapAddressMapPanUpButton;
    private JButton mapAddressMapPanLeftButton;
    private JButton mapAddressMapPanDownButton;
    private JButton mapAddressMapPanRightButton;
    private double mapCenterX;
    private double mapCenterY;
    private String officerLocString;
    private int currentMapZoomHeightAboveEarth;
    private int currentMapImageWidth;
    private int currentMapImageHeight;
    private int mapMouseDownX;
    private int mapMouseDownY;
    private boolean doingMouseDragMap;
    private boolean allowMapMouseDrag = false;

    public OfficerLocationMapPanel(AppWideCallsService pAppWideCallsService,
                                   CustomizedUiWidgetsFactory pCustomizedUiWidgetsFactory) {

        appWideCallsService = pAppWideCallsService;
        customizedUiWidgetsFactory = pCustomizedUiWidgetsFactory;
        currentMapZoomHeightAboveEarth = AppWideStrings.googleMapsDefaultHeightNumber;

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));

        currentOfficerLocationMapPicPanel = new JPanel(new GridLayout(0, 1));
        currentOfficerLocationMapPicLabel = new JLabel();
        currentOfficerLocationMapPicPanel.add(currentOfficerLocationMapPicLabel);
        currentOfficerLocationMapPicPanel.setMinimumSize(new Dimension(50, 50));
        currentOfficerLocationMapPicLabel.setFocusable(true);
        currentOfficerLocationMapPicLabel.setRequestFocusEnabled(true);
        currentOfficerLocationMapPicLabel.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent pE) {
                if (!allowMapMouseDrag) {
                    return;
                }
                if (pE.getWheelRotation() < 0) {
                    currentMapZoomHeightAboveEarth++;
                } else {
                    currentMapZoomHeightAboveEarth--;
                }
                setCurrentOfficerLocationMapFromXYandZ(mapCenterX, mapCenterY,
                        currentMapZoomHeightAboveEarth,
                        AppWideStrings.googleMapsRequestMapSizeWidth,
                        AppWideStrings.googleMapsRequestMapSizeHeight);
                reverseGeocodeCurrentOfficerLocFromXY(officerLocString);
            }
        });

        currentOfficerLocationMapPicLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent pE) {
                mapMouseDownX = pE.getX();
                mapMouseDownY = pE.getY();
            }
        });

        currentOfficerLocationMapPicLabel.addMouseMotionListener(new MouseMotionAdapter() {
            int mouseDragNeeded = 10;
            boolean redrawMap = false;

            @Override
            public void mouseDragged(MouseEvent pE) {
                if (!allowMapMouseDrag) {
                    return;
                }
                if (doingMouseDragMap) {
                    return;
                } else {
                    doingMouseDragMap = true;
                }

                if (Math.abs(pE.getX() - mapMouseDownX) > mouseDragNeeded) {
                    redrawMap = true;
                    if (pE.getX() < mapMouseDownX) {
                        mapCenterY += adjustMapPaningByZoomFactor(((pE.getModifiers()
                                & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK));
                    } else {
                        mapCenterY -= adjustMapPaningByZoomFactor(((pE.getModifiers()
                                & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK));
                    }
                }

                if (Math.abs(pE.getY() - mapMouseDownY) > mouseDragNeeded) {
                    redrawMap = true;
                    if (pE.getY() < mapMouseDownY) {
                        mapCenterX -= adjustMapPaningByZoomFactor(((pE.getModifiers()
                                & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK));
                    } else {
                        mapCenterX += adjustMapPaningByZoomFactor(((pE.getModifiers()
                                & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK));
                    }
                }

                if (redrawMap) {
                    setCurrentOfficerLocationMapFromXYandZ(mapCenterX, mapCenterY,
                            currentMapZoomHeightAboveEarth,
                            currentMapImageWidth, currentMapImageHeight);
                    reverseGeocodeCurrentOfficerLocationFromXY(mapCenterX, mapCenterY);
                }
                doingMouseDragMap = false;
            }
        });

        fullAddressTextArea = new JTextArea(2, 0);
        fullAddressTextArea.setPreferredSize(new Dimension(20, 42));
        crossStreetAddressTextArea = new JTextArea(2, 0);
        crossStreetAddressTextArea.setPreferredSize(new Dimension(20, 42));
        thirdLineAddressTextArea = new JTextArea(2, 0);
        thirdLineAddressTextArea.setPreferredSize(new Dimension(20, 42));
        geoCodeTextField = new JTextField();

        fullAddressTextArea.setLineWrap(true);
        fullAddressTextArea.setWrapStyleWord(true);
        crossStreetAddressTextArea.setLineWrap(true);
        crossStreetAddressTextArea.setWrapStyleWord(true);
        thirdLineAddressTextArea.setLineWrap(true);
        thirdLineAddressTextArea.setWrapStyleWord(true);

        Font textAreaFont = new Font("Arial", Font.BOLD, 12);
        fullAddressTextArea.setFont(textAreaFont);
        crossStreetAddressTextArea.setFont(textAreaFont);
        thirdLineAddressTextArea.setFont(textAreaFont);
        geoCodeTextField.setFont(textAreaFont);

        crossStreetAddressTextArea.setEditable(false);
        thirdLineAddressTextArea.setEditable(false);

        crossStreetAddressTextArea.setBackground(AppWideStrings.lightGray);
        thirdLineAddressTextArea.setBackground(AppWideStrings.lightGray);

        fullAddressTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent pE) {
                fullAddressTextArea.setBackground(Color.WHITE);
                if (pE.getKeyCode() == KeyEvent.VK_ENTER) {
                    pE.consume();
                    String typedAddressString = fullAddressTextArea.getText().trim();
                    if (typedAddressString.length() > 0) {
                        handleShowMapOfFreeTypedInAddress(typedAddressString);
                    }
                }
            }
        });

        geoCodeTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent pE) {
                geoCodeTextField.setBackground(Color.WHITE);
                if (pE.getKeyCode() == KeyEvent.VK_ENTER) {
                    pE.consume();
                    handleMapThisGeoCodeButtonPressed();
                }
            }
        });

        mapAddressMapAboveAddressButton = new JButton(
                AppWideStrings.mapAddressMapAboveAddressButtonString);
        mapAddressCenterOnOfficerButton = new JButton(
                AppWideStrings.mapAddressCenterOnOfficerButtonString);
        mapAddressMapAboveGeocodeButton = new JButton(
                AppWideStrings.mapAddressMapAboveGeocodeButtonString);

        mapAddressMapAboveAddressButton.setToolTipText(
                AppWideStrings.mapAddressMapAboveAddressHoverString);
        mapAddressCenterOnOfficerButton.setToolTipText(
                AppWideStrings.mapAddressCenterOnOfficerHoverString);
        mapAddressMapAboveGeocodeButton.setToolTipText(
                AppWideStrings.mapAddressMapAboveGeocodeHoverString);

        mapAddressMapAboveAddressButton.setPreferredSize(new Dimension(
                (int) mapAddressMapAboveAddressButton.getPreferredSize().getWidth(), 22));
        mapAddressCenterOnOfficerButton.setPreferredSize(new Dimension(
                (int) mapAddressCenterOnOfficerButton.getPreferredSize().getWidth(), 22));
        mapAddressMapAboveGeocodeButton.setPreferredSize(new Dimension(
                (int) mapAddressMapAboveGeocodeButton.getPreferredSize().getWidth(), 22));

        mapAddressMapZoomInButton = customizedUiWidgetsFactory.makeFam3IconButton(
                AppWideStrings.icon_zoom_in, AppWideStrings.icon_zoom_in_HoverHelpText, 26, 20);
        mapAddressMapZoomOutButton = customizedUiWidgetsFactory.makeFam3IconButton(
                AppWideStrings.icon_zoom_out, AppWideStrings.icon_zoom_out_HoverHelpText, 26, 20);
        mapAddressMapPanUpButton = customizedUiWidgetsFactory.makeFam3IconButton(
                AppWideStrings.icon_arrow_up, AppWideStrings.icon_arrow_up_HoverHelpText, 26, 20);
        mapAddressMapPanLeftButton = customizedUiWidgetsFactory.makeFam3IconButton(
                AppWideStrings.icon_arrow_left, AppWideStrings.icon_arrow_left_HoverHelpText, 26, 20);
        mapAddressMapPanDownButton = customizedUiWidgetsFactory.makeFam3IconButton(
                AppWideStrings.icon_arrow_down, AppWideStrings.icon_arrow_down_HoverHelpText, 26, 20);
        mapAddressMapPanRightButton = customizedUiWidgetsFactory.makeFam3IconButton(
                AppWideStrings.icon_arrow_right, AppWideStrings.icon_arrow_rightHoverHelpText, 26, 20);

        mapAddressMapPanUpButton.setEnabled(false);
        mapAddressMapPanLeftButton.setEnabled(false);
        mapAddressMapPanDownButton.setEnabled(false);
        mapAddressMapPanRightButton.setEnabled(false);

        mapAddressMapAboveAddressButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent pE) {
                String typedAddressString = fullAddressTextArea.getText().trim();
                if (typedAddressString.length() > 0) {
                    fullAddressTextArea.setBackground(Color.WHITE);
                    handleShowMapOfFreeTypedInAddress(typedAddressString);
                }
            }
        });

        mapAddressCenterOnOfficerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent pE) {
                setCurrentOfficerLocationFromXYandZ(officerLocString,
                        currentMapZoomHeightAboveEarth,
                        AppWideStrings.googleMapsRequestMapSizeWidth,
                        AppWideStrings.googleMapsRequestMapSizeHeight);
                reverseGeocodeCurrentOfficerLocFromXY(officerLocString);
            }
        });

        mapAddressMapAboveGeocodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent pE) {
                handleMapThisGeoCodeButtonPressed();
            }
        });

        double[][] tableLayoutSpec = new double[][] { {
                TableLayout.FILL,
                TableLayout.PREFERRED,
                TableLayout.FILL
        }, {
                TableLayout.PREFERRED, // 0 "Officer Location"
                TableLayout.PREFERRED, // 1 TextArea
                TableLayout.PREFERRED, // 2 on Right - "Map this Address" Button
                0, // 3
                TableLayout.PREFERRED, // 4 "Cross Street"
                TableLayout.PREFERRED, // 5 TextArea
                2, // 6
                TableLayout.PREFERRED, // 7 "3rd Address"
                TableLayout.PREFERRED, // 8 TextArea
                2, // 9
                TableLayout.PREFERRED, // 10 "GeoCode Point"
                TableLayout.PREFERRED, // 11 TextField
                TableLayout.PREFERRED, // 12 "Center On Officer" button - "Map this GeoCode" button
                2, // 13
                TableLayout.PREFERRED, // 14 mapPanZoomButtonsPanel
                TableLayout.FILL, // 15
                TableLayout.PREFERRED // 16 mapAddressMapZoomLevelLabel
        } };

        isOnRightOfMapImagePanel = new JPanel(new TableLayout(tableLayoutSpec));
        isOnRightOfMapImagePanel.setPreferredSize(new Dimension(286, 50));
        isOnRightOfMapImagePanel.setBackground(AppWideStrings.panelBackgroundColor);
        isOnRightOfMapImagePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 2, 0, 0, Color.GRAY),
                BorderFactory.createEmptyBorder(1, 5, 0, 5)));

        JPanel officerLocLabelPanel = customizedUiWidgetsFactory.applyFontSizeAlignBgColorToLabel(
                12.0f, new JLabel(AppWideStrings.mapAddressOfficerLocationString),
                SwingConstants.LEFT, AppWideStrings.panelBackgroundColor);
        officerLocLabelPanel.setBackground(AppWideStrings.panelBackgroundColor);
        officerLocLabelPanel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
        isOnRightOfMapImagePanel.add(officerLocLabelPanel, "0,0,2,0");

        isOnRightOfMapImagePanel.add(fullAddressTextArea, "0,1,2,1");

        JPanel mapAboveAddressPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        mapAboveAddressPanel.setBackground(AppWideStrings.panelBackgroundColor);
        mapAboveAddressPanel.add(mapAddressMapAboveAddressButton);
        isOnRightOfMapImagePanel.add(mapAboveAddressPanel, "0,2,2,2");

        JPanel crossStreetLabelPanel = customizedUiWidgetsFactory.applyFontSizeAlignBgColorToLabel(
                12.0f, new JLabel(AppWideStrings.mapAddressCrossStreetString),
                SwingConstants.LEFT, AppWideStrings.panelBackgroundColor);
        crossStreetLabelPanel.setBackground(AppWideStrings.panelBackgroundColor);
        crossStreetLabelPanel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
        isOnRightOfMapImagePanel.add(crossStreetLabelPanel, "0,4,2,4");

        isOnRightOfMapImagePanel.add(crossStreetAddressTextArea, "0,5,2,5");

        JPanel thirdAddressLabelPanel = customizedUiWidgetsFactory.applyFontSizeAlignBgColorToLabel(
                12.0f, new JLabel(AppWideStrings.mapAddress3rdAddressString),
                SwingConstants.LEFT, AppWideStrings.panelBackgroundColor);
        thirdAddressLabelPanel.setBackground(AppWideStrings.panelBackgroundColor);
        thirdAddressLabelPanel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
        isOnRightOfMapImagePanel.add(thirdAddressLabelPanel, "0,7,2,7");

        isOnRightOfMapImagePanel.add(thirdLineAddressTextArea, "0,8,2,8");

        JPanel geoCodeLabelPanel = customizedUiWidgetsFactory.applyFontSizeAlignBgColorToLabel(
                12.0f, new JLabel(AppWideStrings.mapAddressGeocodePointString),
                SwingConstants.LEFT, AppWideStrings.panelBackgroundColor);
        geoCodeLabelPanel.setBackground(AppWideStrings.panelBackgroundColor);
        geoCodeLabelPanel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
        isOnRightOfMapImagePanel.add(geoCodeLabelPanel, "0,10,2,10");

        isOnRightOfMapImagePanel.add(geoCodeTextField, "0,11,2,11");

        JPanel mapAboveGeocodePanel = new JPanel(new BorderLayout(0, 0));
        mapAboveGeocodePanel.setBackground(AppWideStrings.panelBackgroundColor);
        mapAboveGeocodePanel.add(mapAddressCenterOnOfficerButton, BorderLayout.WEST);
        mapAboveGeocodePanel.add(mapAddressMapAboveGeocodeButton, BorderLayout.EAST);
        isOnRightOfMapImagePanel.add(mapAboveGeocodePanel, "0,12,2,12");

        JPanel mapPanZoomButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        JPanel mapPanelZoomInOutPanel = new JPanel(new GridLayout(0, 1));
        JPanel mapPanelPanUpDownButtonsPanel = new JPanel(new GridLayout(0, 1));
        JPanel mapPanUpButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        JPanel mapPanDownButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        JPanel mapPanLeftButtonPanel = new JPanel(new GridLayout(0, 1));
        JPanel mapPanRightButtonPanel = new JPanel(new GridLayout(0, 1));

        mapPanZoomButtonsPanel.setBackground(AppWideStrings.panelBackgroundColor);
        mapPanelZoomInOutPanel.setBackground(AppWideStrings.panelBackgroundColor);
        mapPanelPanUpDownButtonsPanel.setBackground(AppWideStrings.panelBackgroundColor);
        mapPanUpButtonPanel.setBackground(AppWideStrings.panelBackgroundColor);
        mapPanDownButtonPanel.setBackground(AppWideStrings.panelBackgroundColor);
        mapPanLeftButtonPanel.setBackground(AppWideStrings.panelBackgroundColor);
        mapPanRightButtonPanel.setBackground(AppWideStrings.panelBackgroundColor);

        mapPanelZoomInOutPanel.add(mapAddressMapZoomInButton);
        mapPanelZoomInOutPanel.add(mapAddressMapZoomOutButton);
        mapPanUpButtonPanel.add(mapAddressMapPanUpButton);
        mapPanDownButtonPanel.add(mapAddressMapPanDownButton);
        mapPanLeftButtonPanel.add(mapAddressMapPanLeftButton);
        mapPanRightButtonPanel.add(mapAddressMapPanRightButton);

        mapPanZoomButtonsPanel.add(mapPanelZoomInOutPanel);
        mapPanelPanUpDownButtonsPanel.add(mapPanUpButtonPanel);
        mapPanelPanUpDownButtonsPanel.add(mapPanDownButtonPanel);

        mapPanZoomButtonsPanel.add(Box.createHorizontalStrut(15));
        mapPanZoomButtonsPanel.add(mapPanLeftButtonPanel);
        mapPanZoomButtonsPanel.add(mapPanelPanUpDownButtonsPanel);
        mapPanZoomButtonsPanel.add(mapPanRightButtonPanel);

        isOnRightOfMapImagePanel.add(mapPanZoomButtonsPanel, "0,14,2,14");

        mapAddressMapZoomLevelLabel = new JLabel("");
        JPanel mapAddressMapZoomLevelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 3));
        mapAddressMapZoomLevelPanel.setBackground(AppWideStrings.panelBackgroundColor);
        mapAddressMapZoomLevelPanel.add(mapAddressMapZoomLevelLabel);
        isOnRightOfMapImagePanel.add(mapAddressMapZoomLevelPanel, "0,16,2,16");

        JPanel mapBorderPanel = new JPanel(new BorderLayout());
        mapBorderPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(7, 7, 7, 7),
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));

        mapBorderPanel.add(currentOfficerLocationMapPicPanel, BorderLayout.CENTER);
        mapBorderPanel.add(isOnRightOfMapImagePanel, BorderLayout.EAST);
        setBackground(Color.WHITE);
        setLayout(new GridLayout(0, 1));
        add(mapBorderPanel);

        mapAddressMapZoomInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent pE) {
                currentMapZoomHeightAboveEarth = Math.min(
                        ++currentMapZoomHeightAboveEarth, AppWideStrings.googleMapsMaxHeightNumber);
                setCurrentOfficerLocationMapFromXYandZ(mapCenterX, mapCenterY,
                        currentMapZoomHeightAboveEarth, currentMapImageWidth, currentMapImageHeight);
            }
        });

        mapAddressMapZoomOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent pE) {
                currentMapZoomHeightAboveEarth = Math.max(
                        --currentMapZoomHeightAboveEarth, AppWideStrings.googleMapsMinHeightNumber);
                setCurrentOfficerLocationMapFromXYandZ(mapCenterX, mapCenterY,
                        currentMapZoomHeightAboveEarth, currentMapImageWidth, currentMapImageHeight);
            }
        });

        mapAddressMapPanUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent pE) {
                mapCenterX += adjustMapPaningByZoomFactor(
                        ((pE.getModifiers() & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK));
                setCurrentOfficerLocationMapFromXYandZ(mapCenterX, mapCenterY,
                        currentMapZoomHeightAboveEarth, currentMapImageWidth, currentMapImageHeight);
                reverseGeocodeCurrentOfficerLocationFromXY(mapCenterX, mapCenterY);
            }
        });

        mapAddressMapPanLeftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent pE) {
                mapCenterY -= adjustMapPaningByZoomFactor(
                        ((pE.getModifiers() & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK));
                setCurrentOfficerLocationMapFromXYandZ(mapCenterX, mapCenterY,
                        currentMapZoomHeightAboveEarth, currentMapImageWidth, currentMapImageHeight);
                reverseGeocodeCurrentOfficerLocationFromXY(mapCenterX, mapCenterY);
            }
        });

        mapAddressMapPanDownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent pE) {
                mapCenterX -= adjustMapPaningByZoomFactor(
                        ((pE.getModifiers() & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK));
                setCurrentOfficerLocationMapFromXYandZ(mapCenterX, mapCenterY,
                        currentMapZoomHeightAboveEarth, currentMapImageWidth, currentMapImageHeight);
                reverseGeocodeCurrentOfficerLocationFromXY(mapCenterX, mapCenterY);
            }
        });

        mapAddressMapPanRightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent pE) {
                mapCenterY += adjustMapPaningByZoomFactor(
                        ((pE.getModifiers() & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK));
                setCurrentOfficerLocationMapFromXYandZ(mapCenterX, mapCenterY,
                        currentMapZoomHeightAboveEarth, currentMapImageWidth, currentMapImageHeight);
                reverseGeocodeCurrentOfficerLocationFromXY(mapCenterX, mapCenterY);
            }
        });
    }

    public void setOfficerLocation(String pOfficerLocString) {
        officerLocString = pOfficerLocString.trim().replaceAll(" ", "");
    }

    public void uponShowPanelChooseXy() {
//        if (appWideCallsService.isOfficerAlarmState()) {
//            setCurrentOfficerLocationFromXYandZ(officerLocString,
//                AppWideStrings.googleMapsDefaultHeightNumber,
//                AppWideStrings.googleMapsRequestMapSizeWidth,
//                AppWideStrings.googleMapsRequestMapSizeHeight);
//
//            reverseGeocodeCurrentOfficerLocFromXY(officerLocString);
//
//        } else {
//            String dispatcherFullMailingAddressString =
//                appWideCallsService.getDispatcherFullMailingAddress();
//
//            if (appWideCallsService.stringHasValue(dispatcherFullMailingAddressString)) {
//                fullAddressTextArea.setText(dispatcherFullMailingAddressString);
//                currentMapZoomHeightAboveEarth = AppWideStrings.googleMapsDefaultCityHeightNumber;
//                handleShowMapOfFreeTypedInAddress(dispatcherFullMailingAddressString);
//
//            } else {
//                setOfficerLocation(
//                    AppWideStrings.gpsSampleXYPoints[0].getX() + "," +
//                        AppWideStrings.gpsSampleXYPoints[0].getY());
//
//                setCurrentOfficerLocationFromXYandZ(officerLocString,
//                    AppWideStrings.googleMapsDefaultHeightNumber,
//                    AppWideStrings.googleMapsRequestMapSizeWidth,
//                    AppWideStrings.googleMapsRequestMapSizeHeight);
//
//                reverseGeocodeCurrentOfficerLocFromXY(officerLocString);
//            }
//        }

        if(appWideCallsService.isHandled())
        {
            setOfficerLocation(appWideCallsService.getLat() + "," + appWideCallsService.getLon());
            setCurrentOfficerLocationFromXYandZ(officerLocString,
                    AppWideStrings.googleMapsDefaultHeightNumber,
                    AppWideStrings.googleMapsRequestMapSizeWidth,
                    AppWideStrings.googleMapsRequestMapSizeHeight);

            reverseGeocodeCurrentOfficerLocFromXY(officerLocString);
        }
        else{
            currentOfficerLocationMapPicLabel.setIcon(null);
        }
    }

    public void initMapImage(){
        currentOfficerLocationMapPicLabel.setIcon(null);
        fullAddressTextArea.setText("");
        crossStreetAddressTextArea.setText("");
        thirdLineAddressTextArea.setText("");
        geoCodeTextField.setText("");
    }

    public double adjustMapPaningByZoomFactor(boolean isControlKeyDown) {
        double multiplyFactor = (AppWideStrings.googleMapsMaxHeightNumber -
                currentMapZoomHeightAboveEarth) + AppWideStrings.googleMapsMinHeightNumber;
        if (isControlKeyDown) {
            return (multiplyFactor * AppWideStrings.googleMapsAmountToControlArrowPan);
        } else {
            return (multiplyFactor * AppWideStrings.googleMapsAmountToArrowPan);
        }
    }

    public void handleMapThisGeoCodeButtonPressed() {
        String geoCodeXYString = geoCodeTextField.getText().trim();
        officerLocString = geoCodeXYString;
        setCurrentOfficerLocationFromXYandZ(geoCodeXYString,
                currentMapZoomHeightAboveEarth,
                AppWideStrings.googleMapsRequestMapSizeWidth,
                AppWideStrings.googleMapsRequestMapSizeHeight);
        reverseGeocodeCurrentOfficerLocFromXY(geoCodeXYString);
    }

    public void setCurrentOfficerLocationMapFromXYandZ(
            double pXgps, double pYgps, int pHeightAboveEarth, int pImageSizeX, int pImageSizeY) {

        String xAndYString = pXgps + "," + pYgps;
        setCurrentOfficerLocationFromXYandZ(xAndYString, pHeightAboveEarth,
                pImageSizeX, pImageSizeY);
    }

    public void setCurrentOfficerLocationFromXYandZ(String pXAndYString,
                                                    int pHeightAboveEarth, int pImageSizeX, int pImageSizeY) {

        if (appWideCallsService.stringIsNullOrEmpty(pXAndYString)) {
            return;
        }

        String mapThisGeoCodeString = pXAndYString.trim().replaceAll(" ", "");
        if (!(mapThisGeoCodeString.contains(","))) {
            return;
        } else {
            currentMapZoomHeightAboveEarth = pHeightAboveEarth;
            currentMapImageWidth = pImageSizeX;
            currentMapImageHeight = pImageSizeY;
            mapAddressMapZoomLevelLabel.setText(AppWideStrings.googleMapsZoomLabelString +
                    currentMapZoomHeightAboveEarth);
        }

        String setMapTypeString = currentMapZoomHeightAboveEarth > 16 ? "hybrid" : "roadmap";
        byte[] mapByteArray = null;

        try {
            String imageSpecString = AppWideStrings.googleMapsRequestStaticMapUrlString
                    + mapThisGeoCodeString
                    + "&zoom="
                    + pHeightAboveEarth
                    + "&size="
                    + pImageSizeX + "x" + pImageSizeY
                    + "&scale=1"
                    + "&format=png"
                    + "&maptype=" + setMapTypeString
                    // + "&markers=color:red%7Clabel:P%7C"
                    // + officerLocString
                    + "&markers=icon:" + AppWideStrings.mapAddressCenterOfMapIconUrlString + "%7C"
                    + mapThisGeoCodeString
                    + "&key="
                    + developmentTimeMapsApiKey;

//            System.out.println(imageSpecString);

            // File previousMapFile = new File(AppWideStrings.googleMapsReceiveFileLoc
            //    + AppWideStrings.googleMapsCurrentOfficerFilename);
            // Delete the previous map file
            // previousMapFile.delete();

            URL mapUrl = new URL(imageSpecString);
            InputStream mapInputStream = mapUrl.openStream();

            ByteArrayOutputStream baOutputStream =
                    new ByteArrayOutputStream(AppWideStrings.googleMapsReadBytesNeeded);

            // FileOutputStream aFileOutputStream =
            //     new FileOutputStream(AppWideStrings.googleMapsReceiveFileLoc
            //     + AppWideStrings.googleMapsCurrentOfficerFilename);

            byte[] byteArray = new byte[40960];
            int readLength;

            while ((readLength = mapInputStream.read(byteArray)) != -1) {
                baOutputStream.write(byteArray, 0, readLength);
            }

            baOutputStream.flush();
            mapByteArray = baOutputStream.toByteArray();

            mapInputStream.close();
            baOutputStream.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        currentOfficerGoogleMapsBufferedImage = null;
        currentOfficerGoogleMapsImage = null;
        try {
            ByteArrayInputStream baInputStream = new ByteArrayInputStream(mapByteArray);

            // InputStream currentOfficerGoogleMapsFileInputStream = getClass()
            //     .getResourceAsStream(AppWideStrings.googleMapsReadFileSubdirLoc
            //     + AppWideStrings.googleMapsCurrentOfficerFilename);

            if (baInputStream != null) {
                currentOfficerGoogleMapsBufferedImage = ImageIO.read(baInputStream);
            }

            mapByteArray = null;
            baInputStream.close();
            baInputStream = null;

        } catch (IOException ex) {
            System.err.println(
                    "app - OfficerLocationMapPanel.setCurrentOfficerLocationFromXYandZ() "
                            + " could not read google map "
                            // + " could not read map file "
                            // + AppWideStrings.googleMapsCurrentOfficerFilename
                            + " - " + ex.getMessage());
        }

        if (currentOfficerGoogleMapsBufferedImage != null && appWideCallsService.isHandled()) {
            currentOfficerGoogleMapsImage = currentOfficerGoogleMapsBufferedImage
                    .getScaledInstance(Math.round(
                            pImageSizeX * 1.4f), Math.round(pImageSizeY * 1.4f),
                            Image.SCALE_SMOOTH);
            // currentOfficerLocationMapPicLabel.setIcon(null);
            currentOfficerLocationMapPicLabel.setIcon(new ImageIcon(
                    currentOfficerGoogleMapsImage));
        }
    }

    public void reverseGeocodeCurrentOfficerLocationFromXY(double pXgps, double pYgps) {
        String XAndYString = pXgps + "," + pYgps;
        reverseGeocodeCurrentOfficerLocFromXY(XAndYString);
    }

    public void reverseGeocodeCurrentOfficerLocFromXY(String pXAndYString) {
        String mapThisGeoCodeString = null;
        if(!appWideCallsService.isHandled()) return;
        if (appWideCallsService.stringIsNullOrEmpty(pXAndYString)) {
            mapThisGeoCodeString = AppWideStrings.gpsSampleXYPoints[0].getX() + "," +
                    AppWideStrings.gpsSampleXYPoints[0].getY();
        } else {
            mapThisGeoCodeString = pXAndYString.trim().replaceAll(" ", "");
        }

        if (!(mapThisGeoCodeString.contains(","))) {
            geoCodeTextField.setBackground(Color.RED);
            return;
        } else {
            String[] xOrYGeoCodeStringArray = mapThisGeoCodeString.split(",");
            mapCenterX = 0;
            mapCenterY = 0;
            try {
                mapCenterX = truncateDoubleToNdecimalPlaces(xOrYGeoCodeStringArray[0], 6);
                mapCenterY = truncateDoubleToNdecimalPlaces(xOrYGeoCodeStringArray[1], 6);
            } catch (NullPointerException | NumberFormatException ex) {
                ex.printStackTrace();
                geoCodeTextField.setBackground(Color.RED);
                return;
            }
        }

        geoCodeTextField.setText(mapCenterX + "," + mapCenterY);
        ByteArrayOutputStream aOutputStream = null;
        try {
            String requestSpecString = AppWideStrings.googleMapsRequestCrossStreetsUrlString
                    + mapThisGeoCodeString
                    + "&key="
                    + developmentTimeMapsApiKey;

//            System.out.println(requestSpecString);

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

            // String jSonString = aOutputStream.toString();
            // System.out.println(jSonString);

        } catch (IOException ex) {
            ex.printStackTrace();
            geoCodeTextField.setBackground(Color.RED);
            return;
        }

        JsonReader aJsonReader = Json.createReader(
                new ByteArrayInputStream(aOutputStream.toByteArray()));

        JsonObject aJsonObjectOuter = aJsonReader.readObject();
        if (!(aJsonObjectOuter.getString("status").equalsIgnoreCase("OK"))) {
            geoCodeTextField.setBackground(Color.RED);
            return;
        }

        JsonArray aJsonArrayResults = aJsonObjectOuter.getJsonArray("results");

        if (aJsonArrayResults == null) {
            geoCodeTextField.setBackground(Color.RED);
            return;
        }

        Iterator<JsonValue> aIterator = aJsonArrayResults.iterator();

        int index = 0;
        fullAddressString = "";
        crossStreetAddressString = "";
        thirdLineAddressString = "";

        while (aIterator.hasNext()) {
            JsonValue aJsonValueAddresses = aIterator.next();
            if (aJsonValueAddresses.getValueType() == JsonValue.ValueType.OBJECT) {
                JsonObject aJsonAddresesObject = (JsonObject) aJsonValueAddresses;
                String addressString = aJsonAddresesObject.getString("formatted_address");
                if ((addressString != null) && (addressString.length() > 0)) {
                    if (index == 0) {
                        fullAddressString = addressString;
//                        System.out.println("Full Address is " + fullAddressString);

                    } else if (index == 1) {
                        crossStreetAddressString = addressString;
//                        System.out.println("Cross Street Address is " + crossStreetAddressString);

                    } else if (index == 2) {
                        thirdLineAddressString = addressString;
                        if (addressString.contains("/")) {
//                            System.out.println("Cross Street Address is " + thirdLineAddressString);
                        } else {
//                            System.out.println("3rd Address is " + thirdLineAddressString);
                        }

                    } else if (addressString.contains("/")) {
                        crossStreetAddressString = addressString;
//                        System.out.println("Cross Street Address is " + crossStreetAddressString);
                    } else {
//                        System.out.println("Generalized Address is " + addressString);
                    }
                }
            }
            index++;
        }
        fullAddressTextArea.setText(fullAddressString);
        crossStreetAddressTextArea.setText(crossStreetAddressString);
        thirdLineAddressTextArea.setText(thirdLineAddressString);
        currentOfficerLocationMapPicLabel.requestFocusInWindow();
    }

    private void handleShowMapOfFreeTypedInAddress(String pTypedAddressString) {
        geocodeCurrentOfficerLocFromFreeTypedAddress(pTypedAddressString);
        setCurrentOfficerLocationFromXYandZ(officerLocString,
                currentMapZoomHeightAboveEarth,
                AppWideStrings.googleMapsRequestMapSizeWidth,
                AppWideStrings.googleMapsRequestMapSizeHeight);
        reverseGeocodeCurrentOfficerLocFromXY(officerLocString);
    }

    private void geocodeCurrentOfficerLocFromFreeTypedAddress(String pFreeTypedAddressString) {
        int responseCode = 0;
        ByteArrayOutputStream aOutputStream = null;
        InputStream aInputStream = null;

        try {
            String requestSpecString = AppWideStrings.googleMapsGetXyFromAddressUrlString
                    + URLEncoder.encode(pFreeTypedAddressString, "UTF-8")
                    + "&key="
                    + developmentTimeMapsApiKey;

//            System.out.println(requestSpecString);

            URL sendUrl = new URL(requestSpecString);
            HttpURLConnection httpConnection = (HttpURLConnection) sendUrl.openConnection();
            httpConnection.connect();
            responseCode = httpConnection.getResponseCode();
            if (responseCode > 299) {
                throw new IOException("Turning typed address onto Geocode, Response code="
                        + responseCode);
            }

            aInputStream = httpConnection.getInputStream();
            aOutputStream = new ByteArrayOutputStream(2048);

            byte[] byteArray = new byte[2048];
            int readLength = 0;

            while ((readLength = aInputStream.read(byteArray)) != -1) {
                aOutputStream.write(byteArray, 0, readLength);
            }

            aInputStream.close();
            aOutputStream.flush();
            aOutputStream.close();

            // String jSonString = aOutputStream.toString();
            // System.out.println(jSonString);

        } catch (IOException ex1) {
            ex1.printStackTrace();
            fullAddressTextArea.setBackground(Color.RED);
            crossStreetAddressTextArea.setText("");
            return;
        }

        JsonReader aJsonReader = Json.createReader(
                new ByteArrayInputStream(aOutputStream.toByteArray()));

        JsonObject aJsonObjectOuter = aJsonReader.readObject();
        String statusString = aJsonObjectOuter.getString("status");
        if (!(statusString.equalsIgnoreCase("OK"))) {
            fullAddressTextArea.setBackground(Color.RED);
            crossStreetAddressTextArea.setText("Error - " + statusString);
            return;
        }

        JsonArray aJsonArrayResults = aJsonObjectOuter.getJsonArray("results");

        if (aJsonArrayResults == null) {
            fullAddressTextArea.setBackground(Color.RED);
            crossStreetAddressTextArea.setText("Error - Json has no Results.");
            return;
        }

        Iterator<JsonValue> aIterator = aJsonArrayResults.iterator();

        String xyValueString = "";

        while (aIterator.hasNext()) {
            JsonValue aJsonValue = aIterator.next();
            if (aJsonValue.getValueType() == JsonValue.ValueType.OBJECT) {
                JsonObject aJsonObject = (JsonObject) aJsonValue;
                JsonObject geometryObject = aJsonObject.getJsonObject("geometry");
                if (geometryObject != null) {
                    JsonObject locationObject = geometryObject.getJsonObject("location");
                    if (locationObject != null) {
                        JsonNumber xJsonNumber = locationObject.getJsonNumber("lat");
                        JsonNumber yJsonNumber = locationObject.getJsonNumber("lng");
                        if ((xJsonNumber != null) && (yJsonNumber != null)) {
                            xyValueString = xJsonNumber + "," + yJsonNumber;
                            officerLocString = xyValueString;
                            break;
                        }
                    }
                }
            }
        }
        if (xyValueString.length() < 1) {
            fullAddressTextArea.setBackground(Color.RED);
            crossStreetAddressTextArea.setText("Error - Google Maps can't find address");
        }
    }

    private double truncateDoubleToNdecimalPlaces(
            String pInputDoubleString, int pKeepNdecimalPlaces) {

        double powerOf10 = Math.pow(10, pKeepNdecimalPlaces);
        double originalDouble = Double.parseDouble(pInputDoubleString);
        long turnIntoLong = Math.round(originalDouble * powerOf10);
        double returnDouble = turnIntoLong / powerOf10;
        return returnDouble;
    }

    //    public static void getAddressFromLocation(
    //        final Location location, final Context context, final Handler handler) {
    //        Thread thread = new Thread() {
    //            @Override public void run() {
    //                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
    //                String result = null;
    //                try {
    //                    List<Address> list = geocoder.getFromLocation(
    //                        location.getLatitude(), location.getLongitude(), 1);
    //                    if (list != null && list.size() > 0) {
    //                        Address address = list.get(0);
    //                        // sending back first address line and locality
    //                        result = address.getAddressLine(0) + ", " + address.getLocality();
    //                    }
    //                } catch (IOException e) {
    //                    Log.e(TAG, "Impossible to connect to Geocoder", e);
    //                } finally {
    //                    Message msg = Message.obtain();
    //                    msg.setTarget(handler);
    //                    if (result != null) {
    //                        msg.what = 1;
    //                        Bundle bundle = new Bundle();
    //                        bundle.putString("address", result);
    //                        msg.setData(bundle);
    //                    } else
    //                        msg.what = 0;
    //                    msg.sendToTarget();
    //                }
    //            }
    //        };
    //        thread.start();
    //    }
}
