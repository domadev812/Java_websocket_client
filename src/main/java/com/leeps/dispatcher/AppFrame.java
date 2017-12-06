package com.leeps.dispatcher;

import com.leeps.dispatcher.common.*;
import com.leeps.dispatcher.dialogs.*;
import com.leeps.dispatcher.material.MaterialButton;
import com.leeps.dispatcher.panels.OfficerLocationMapPanel;
import com.leeps.dispatcher.panels.OfficerProfilePanel;
import com.leeps.dispatcher.panels.OfficerStatusGraphPanel;
import com.leeps.dispatcher.service.*;
import com.leeps.dispatcher.uidatamodel.*;
import de.craften.ui.swingmaterial.fonts.Roboto;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import oracle.jvm.hotspot.jfr.JFR;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Properties;

public class AppFrame extends JFrame {
    private AppFrame thisAppFrame;

    // Dispatcher Profile Variable
    private int dispatcherID;
    private JSONObject dispatcherProfile = new JSONObject();
    JSONObject handledOfficer = null;
    boolean isHandled;
    double lat = 40.126936, lon = 124.394631;

    //Service and Constant Class Variable
    private AppWideCallsService appWideCallsService;
    private CustomizedUiWidgetsFactory customizedUiWidgetsFactory;
    private AppCommon common = new AppCommon();

    //Image Icons, Cursor
    private Cursor handPointingCursor;
    private ImageIcon genericUserImageIcon;
    private BufferedImage officerProfileImage;
    private BufferedImage appIcon1BufferedImage;
    private BufferedImage appIcon2BufferedImage;
    private ImageIcon connectedImageIcon;
    private ImageIcon disconnectedImageIcon;
    private enum WhichAppIcon {
        APP_ICON_1, APP_ICON_2
    }
    private Thread iconBlinkThread;

    //Menu Component
    private JMenuBar appMenuBar;
    private JMenu dispatcherProfileMenu;
    private JMenu windowMenu;
    private JMenu helpMenu;
    private MaterialButton alarmsPendingButton;
    private BaseMenuItem currentMenuItem = null;

    //UI Component
    private LoginDialog loginDialog;
    private DispatcherProfileDialog dispatcherProfileDialog = null;
    private AlarmsPendingDialog alarmsPendingDialog = null;
    private JPanel contentPanel;
    private JPanel centerPanel;
    private JPanel bottomPanel;

    private JLabel lblApplicationStatus = new JLabel();
    private JLabel lblConnection = new JLabel();
    private JLabel lblConnectionImage = new JLabel();

    private Rectangle preferredAppLocationAndSize;

    private OfficerStatusGraphPanel officerStatusGraphPanel;
    private OfficerLocationMapPanel officerLocationMapPanel;
    private OfficerProfilePanel officerProfilePanel;
    private JSplitPane hasLeftAndRightPanelsJSplitPane;
    private JSplitPane hasTopAndBottomPanelsJSplitPane;

    private ArrayList<StateModel> listState = new ArrayList<StateModel>();
    private ArrayList<CityModel> listCity = new ArrayList<CityModel>();
    private ArrayList<StationModel> listStation = new ArrayList<StationModel>();
    private ArrayList<DispatchStationModel> listDispatcherStation = new ArrayList<DispatchStationModel>();

    private ArrayList<JSONObject> waitingOfficerList = new ArrayList<JSONObject>();
    int howManyAlarmsPending = 0;

    //Socket Variables
    private Socket socket;
    final private String serverIP = "192.168.0.100";
//    final private String serverIP = "ec2-user@ec2-34-213-184-150.us-west-2.compute.amazonaws.com";
    final private int SERVER_PORT = 8120;

    public AppFrame() {
        thisAppFrame = this;
        //setUndecorated(true);
        appWideCallsService = new AppWideCallsService();
        appWideCallsService.setAppFrame(this);

        Thread threadConnection = new Thread(new Runnable() {
            public void run() {
                try {
                    initConnection();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        threadConnection.start();


        isHandled = false;
        initCustomizedUiWidgetsFactory();
        initProperties();
        layoutUI();
        alarmsPendingDialog = new AlarmsPendingDialog(this, 640, 250, customizedUiWidgetsFactory, appWideCallsService);
        setContentPane(contentPanel);;
//        addWindowListener(new FrameWindowListener());
//        addComponentListener(new FrameResizedListener());

        setJMenuBar(appMenuBar);
        setTitle(AppWideStrings.appTitle);
        makeAppPreferredAppLocationAndSize();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        initPendingData();
        loginDialog = new LoginDialog(this, 640, 390, appWideCallsService);
        loginDialog.setVisible(true);
        setVisible(true);
    }

    private void initPendingData() {
        for(int i = 0; i < 3; i++) {
            JSONObject jsonObject1 = new JSONObject();
            try {
                jsonObject1.put(KeyStrings.keyFirstName, "First" + i);
                jsonObject1.put(KeyStrings.keyLastName, "Last1" + i);
                jsonObject1.put(KeyStrings.keyBadgeNumber, "Badge" + i);
                jsonObject1.put(KeyStrings.keyLatitude, 123.4567);
                jsonObject1.put(KeyStrings.keyLongitude, 38.9865);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            addWaitingOfficer(jsonObject1);
        }
    }

    //Data Manage Functions
    public void setStateList(ArrayList<StateModel> listState) {this.listState = listState;}
    public ArrayList<StateModel> getStateList() {return this.listState;}

    public void setCityList(ArrayList<CityModel> listCity) {this.listCity = listCity;}
    public ArrayList<CityModel> getCityList() {return this.listCity;}

    public void setStationList(ArrayList<StationModel> listStation) {this.listStation = listStation;}
    public ArrayList<StationModel> getStationList() {return this.listStation;}

    public void setDispatchStationList(ArrayList<DispatchStationModel> mapDispatchStation) {this.listDispatcherStation = mapDispatchStation;}
    public ArrayList<DispatchStationModel> getDispatchStationList() {return this.listDispatcherStation;}

    public JSONObject getDispatchObject() {return this.dispatcherProfile;}
    public int getDispatcherID() {return dispatcherID;}

    public void setHandledOfficer(JSONObject jsonObject){this.handledOfficer = jsonObject;}
    public JSONObject getHandledOfficer() {return this.handledOfficer;}

    public void addWaitingOfficer(JSONObject jsonObject) {
        waitingOfficerList.add(jsonObject);
        updateHowManyAlarmsPending();
    }

    public void setHandled(boolean isHandled) {this.isHandled = isHandled;}
    public boolean isHandled() {return this.isHandled;}

    public void setLat(double lat){this.lat = lat;}
    public double getLat(){return this.lat;}

    public void setLon(double lon){this.lon = lon;}
    public double getLon(){return this.lon;}
    //Init Functions
    private void initProperties() {
        Properties aProperties = new Properties();
        InputStream aInputStream = null;

        try {
            aInputStream = new FileInputStream("./resources/" +
                    AppWideStrings.appSettingsPropertiesFileNameString);
            aProperties.load(aInputStream);

        } catch (IOException pEx) {
            pEx.printStackTrace();
        } finally {
            if (aInputStream != null) {
                try {
                    aInputStream.close();
                } catch (IOException pEx2) {
                    pEx2.printStackTrace();
                }
            }
        }
    }

    private void initImages() {
        try {
            InputStream genericOfficerPicInputStream = getClass()
                    .getResourceAsStream(AppWideStrings.emptyOfficerProfileImg);
            if (genericOfficerPicInputStream != null) {
                officerProfileImage = ImageIO.read(genericOfficerPicInputStream);
            }
        } catch (IOException ex) {
            System.err.println("app - initImages. Could not read an officer profile picture - "
                    + ex.getMessage());
        }
    }

    private void initAppIcon() {
        try {
            InputStream imageInputStream1 = getClass().getResourceAsStream(
                    AppWideStrings.appIcon1Loc);
            InputStream imageInputStream2 = getClass().getResourceAsStream(
                    AppWideStrings.appIcon2Loc);
            if ((imageInputStream1 != null) && (imageInputStream2 != null)) {
                appIcon1BufferedImage = ImageIO.read(imageInputStream1);
                appIcon2BufferedImage = ImageIO.read(imageInputStream2);
            }
        } catch (IOException ex) {
            System.err.println("app - initAppIcon. Could not read app icon");
        }
    }

    private void makeAppPreferredAppLocationAndSize() {
        preferredAppLocationAndSize = appWideCallsService.positionAndSize27inchWideMonitor();
        setBounds(preferredAppLocationAndSize);
    }

    private void changeConnectionState(boolean flag) {
        if(flag) {
            lblConnection.setText(AppWideStrings.socketConnectionString);
            lblConnectionImage.setIcon(connectedImageIcon);
        }
        else {
            lblConnection.setText(AppWideStrings.socketDisconnectionString);
            lblConnectionImage.setIcon(disconnectedImageIcon);
        }
    }

    private void updateHowManyAlarmsPending() {
        howManyAlarmsPending = waitingOfficerList.size();
        alarmsPendingButton.setText(AppWideStrings.alarmPendingButtonString + howManyAlarmsPending);

        if (howManyAlarmsPending == 0) {
            alarmsPendingButton.setConfiguration(new Color(0x4F, 0x4F, 0x4F), Color.WHITE, new Color(0x4F, 0x4F, 0x4F));
            blinkAppIcon(false);
        } else {
            alarmsPendingButton.setConfiguration(new Color(0xEC, 0x19, 0x19), Color.WHITE, new Color(0xDB, 0x05, 0x05));
            if (iconBlinkThread == null) {
                blinkAppIcon(true);
            }
        }

        alarmsPendingDialog.replaceRowsNameListPanel(waitingOfficerList);
    }
    public void blinkAppIcon(boolean pShouldBlink) {
        if (!pShouldBlink) {
            if (iconBlinkThread != null) {
                iconBlinkThread.interrupt();
                iconBlinkThread = null;
            }

            setState(Frame.NORMAL);
            setTheAppIcon(WhichAppIcon.APP_ICON_1);
            toFront();
//            appWideCallsService.stopAppIconBlinkingSound();

        } else {
            iconBlinkThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        if ((iconBlinkThread != null) && iconBlinkThread.isAlive()
                                && !(iconBlinkThread.isInterrupted())) {
                            try {
                                setTheAppIcon(WhichAppIcon.APP_ICON_2);
//                                appWideCallsService.playAppIconBlinkingSound();
                                Thread.sleep(500);

                                setTheAppIcon(WhichAppIcon.APP_ICON_1);
                                Thread.sleep(500);
                            } catch (InterruptedException ex) {
                            }
                        } else {
                            // User clicks blinking icon when minimized
                            blinkAppIcon(false);
                            break;
                        }
                    }
                }
            });
            iconBlinkThread.start();
        }
    }

    // UI Effect Service Functions
    private void initCustomizedUiWidgetsFactory() {
        customizedUiWidgetsFactory = new CustomizedUiWidgetsFactory();
        genericUserImageIcon = customizedUiWidgetsFactory.makeImageIcon(
                AppWideStrings.icon_generic_user_green);
        connectedImageIcon = new ImageIcon(getClass().getResource(AppWideStrings.connectedImage));
        disconnectedImageIcon = new ImageIcon(getClass().getResource(AppWideStrings.offlineImage));
    }

    private void applyMenuEffect(final JMenu pJMenu) {
        pJMenu.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        pJMenu.setBackground(AppWideStrings.primaryColor);
        pJMenu.setOpaque(true);
        pJMenu.setCursor(handPointingCursor);
        pJMenu.setForeground(Color.WHITE);
        pJMenu.setFont(Roboto.BOLD.deriveFont(14.0f));
        pJMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent pE) {
                super.mouseEntered(pE);
                pJMenu.setForeground(Color.GREEN);
            }

            @Override
            public void mouseExited(MouseEvent pE) {
                super.mouseExited(pE);
                pJMenu.setForeground(Color.WHITE);
            }
        });
    }

    // UI Component Build Functions
    private void layoutUI() {
        buildMenuBar();
        initImages();
        initAppIcon();
        setTheAppIcon(WhichAppIcon.APP_ICON_1);
        buildCenterPanel();
        buildBottomPanel();
        buildContentPanel();
    }

    private void setTheAppIcon(WhichAppIcon pEnumWhichIcon) {
        if (pEnumWhichIcon == WhichAppIcon.APP_ICON_1) {
            setIconImage(appIcon1BufferedImage);
        } else {
            setIconImage(appIcon2BufferedImage);
        }
    }

    private void buildMenuBar() {
        appMenuBar = new JMenuBar() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(AppWideStrings.primaryColor);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };

        appMenuBar.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        dispatcherProfileMenu = new JMenu(AppWideStrings.menuBarDispatcherString);
        dispatcherProfileMenu.setOpaque(true);
        BaseMenuItem itemManage = new BaseMenuItem("MANAGE STATION");
        BaseMenuItem itemPassword = new BaseMenuItem("CHANGE PASSWORD");
        BaseMenuItem itemExit = new BaseMenuItem("EXIT");

        dispatcherProfileMenu.add(itemManage);
        dispatcherProfileMenu.add(itemPassword);
        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setBackground(AppWideStrings.primaryColor);
        sep.setForeground(Color.WHITE);
        sep.setOpaque(true);
        dispatcherProfileMenu.add(sep);
        dispatcherProfileMenu.add(itemExit);

        windowMenu = new JMenu(
                AppWideStrings.menuBarWindowString);
        final BaseMenuItem item24Display = new BaseMenuItem("24INCH DISPLAY");
        final BaseMenuItem item27Display = new BaseMenuItem("27INCH DISPLAY");
        windowMenu.add(item24Display);
        windowMenu.add(item27Display);

        helpMenu = new JMenu(AppWideStrings.menuBarHelpString);
        BaseMenuItem itemDisclaimer = new BaseMenuItem("DISCLAIMER");
        BaseMenuItem itemPrivacy = new BaseMenuItem("PRIVACY");
        BaseMenuItem itemTerms = new BaseMenuItem("TERMS");
        BaseMenuItem itemFaq = new BaseMenuItem("FAQ");
        helpMenu.add(itemDisclaimer);
        helpMenu.add(itemPrivacy);
        helpMenu.add(itemTerms);
        helpMenu.add(itemFaq);

        applyMenuEffect(dispatcherProfileMenu);
        applyMenuEffect(windowMenu);
        applyMenuEffect(helpMenu);

        handPointingCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

        alarmsPendingButton = new MaterialButton(AppWideStrings.alarmPendingButtonString + howManyAlarmsPending, new Color(0x4F, 0x4F, 0x4F), Color.WHITE, new Color(0x4F, 0x4F, 0x4F));
        alarmsPendingButton.setFont(common.getRobotoBoldFont(14.0f));
        alarmsPendingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent pE) {
                alarmsPendingDialog.setVisible(true);
            }
        });

        JPanel menuAlarmPendingPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 0, 5));
        menuAlarmPendingPanel.setBackground(AppWideStrings.primaryColor);
        menuAlarmPendingPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        menuAlarmPendingPanel.add(Box.createHorizontalStrut(8));
        menuAlarmPendingPanel.add(alarmsPendingButton);

        appMenuBar.add(dispatcherProfileMenu);
        appMenuBar.add(windowMenu);
        appMenuBar.add(helpMenu);
        appMenuBar.add(menuAlarmPendingPanel);

        itemManage.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent pE) {
                dispatcherProfileDialog = new DispatcherProfileDialog(thisAppFrame,1120, 800, appWideCallsService);
            }
        });

        itemPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent pE) {
                new ChangePwdDialog(thisAppFrame,500, 420, appWideCallsService);
            }
        });

        itemExit.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent pE) {
                System.exit(0);
            }
        });

        item24Display.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent pE) {
                if(currentMenuItem != null) {
                    currentMenuItem.setForeground(Color.WHITE);
                }
                currentMenuItem = item24Display;
                currentMenuItem.setForeground(Color.GREEN);
                setBounds(appWideCallsService.positionAndSize24inchWideMonitor());
            }
        });

        item27Display.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent pE) {
                if(currentMenuItem != null) {
                    currentMenuItem.setForeground(Color.WHITE);
                }
                currentMenuItem = item27Display;
                currentMenuItem.setForeground(Color.GREEN);
                setBounds(appWideCallsService.positionAndSize27inchWideMonitor());
            }
        });
        helpMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent pE) {
                // showHelpPanel();
            }
        });
    }

    private void buildCenterPanel() {
        officerStatusGraphPanel = new OfficerStatusGraphPanel(
                customizedUiWidgetsFactory, appWideCallsService);
        appWideCallsService.setOfficerStatusGraphPanel(officerStatusGraphPanel);

        officerProfilePanel = new OfficerProfilePanel(appWideCallsService);
        appWideCallsService.setOfficerProfilePanel(officerProfilePanel);
        appWideCallsService.setCurrentOfficerPicBufferedImage(officerProfileImage);

        officerLocationMapPanel = new OfficerLocationMapPanel(
                appWideCallsService, customizedUiWidgetsFactory);
        appWideCallsService.setOfficerLocationMapPanel(officerLocationMapPanel);

        hasLeftAndRightPanelsJSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        hasLeftAndRightPanelsJSplitPane.setLeftComponent(officerProfilePanel);
        hasLeftAndRightPanelsJSplitPane.setRightComponent(officerLocationMapPanel);
        hasLeftAndRightPanelsJSplitPane.setBorder(BorderFactory.createEmptyBorder());
        JPanel theTopPanelOfTopBottomSplitPane = new JPanel(new GridLayout(0, 1));
        theTopPanelOfTopBottomSplitPane.add(hasLeftAndRightPanelsJSplitPane);
        theTopPanelOfTopBottomSplitPane.setBackground(Color.RED);
        theTopPanelOfTopBottomSplitPane.setOpaque(true);
        theTopPanelOfTopBottomSplitPane.setBorder(BorderFactory.createEmptyBorder());

        hasTopAndBottomPanelsJSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
        hasTopAndBottomPanelsJSplitPane.setTopComponent(theTopPanelOfTopBottomSplitPane);
        hasTopAndBottomPanelsJSplitPane.setBottomComponent(officerStatusGraphPanel);
        hasTopAndBottomPanelsJSplitPane.setBackground(Color.RED);
        hasTopAndBottomPanelsJSplitPane.setOpaque(true);
        hasTopAndBottomPanelsJSplitPane.setBorder(BorderFactory.createEmptyBorder());

        centerPanel = new JPanel();
        centerPanel.setLayout(new CardLayout());
        centerPanel.setBackground(Color.RED);
        centerPanel.setOpaque(true);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(
                        0, 0, 0, 0),
                BorderFactory.createLineBorder(Color.GRAY)));
        centerPanel.add(hasTopAndBottomPanelsJSplitPane,
                AppWideStrings.centerOfficerPanelCardLayoutKey);
        hasTopAndBottomPanelsJSplitPane.setDividerSize(3);
        hasLeftAndRightPanelsJSplitPane.setDividerSize(5);
    }

    private void buildBottomPanel() {
        lblApplicationStatus.setText(AppWideStrings.applicationStatusString + " " + AppWideStrings.applicationAwaitingOfficers);
        lblApplicationStatus.setForeground(Color.WHITE);
        lblApplicationStatus.setFont(common.getRobotoBoldFont(14.0f));

        lblConnection.setText(AppWideStrings.socketConnectionString);
        lblConnection.setForeground(Color.WHITE);
        lblConnection.setFont(common.getRobotoBoldFont(10.0f));

        lblConnectionImage.setForeground(Color.WHITE);
        lblConnectionImage.setFont(common.getRobotoBoldFont(10.0f));
        lblConnectionImage.setIcon(connectedImageIcon);

        JPanel leftButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        leftButtonPanel.add(lblApplicationStatus);
        leftButtonPanel.setOpaque(false);

        JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        JPanel connectionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        connectionPanel.add(lblConnection);
        connectionPanel.add(Box.createHorizontalStrut(10));
        connectionPanel.add(lblConnectionImage);
        connectionPanel.setOpaque(false);
        rightButtonPanel.add(connectionPanel);
        rightButtonPanel.setOpaque(false);

        bottomPanel = new JPanel(new BorderLayout(0, 0));
        bottomPanel.add(leftButtonPanel, BorderLayout.WEST);
        bottomPanel.add(rightButtonPanel, BorderLayout.EAST);
        bottomPanel.setBackground(AppWideStrings.primaryColor);
        bottomPanel.setSize(new Dimension(bottomPanel.getWidth(), 50));
    }

    private void buildContentPanel() {
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(centerPanel, BorderLayout.CENTER);
        // contentPaneJPanel.add(topBarPanel, BorderLayout.NORTH);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);
        showHandledOfficer();
    }
    // UI Component Choose Functions
    private void showHandledOfficer() {
        System.out.println("Show Officer Profile");
        ((CardLayout) (centerPanel.getLayout())).show(
                centerPanel, AppWideStrings.centerOfficerPanelCardLayoutKey);
        appWideCallsService.showOfficerProfileUiDataModel(handledOfficer);
        showAssistanceMap();
    }

    public void showAssistanceMap() {
//        officerLocationMapPanel.uponShowPanelChooseXy();
    }
    //Callback Process Functions
    public void logIn(int errorCode, JSONObject jsonObject)     //Login CallBack
    {
        if(errorCode == 0) {
            try {
                dispatcherID = jsonObject.getInt(KeyStrings.keyID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dispatcherProfile = jsonObject;
            loginDialog.dispose();
        } else {
            loginDialog.showErrorMessage("E-Mail or Password is incorrect.");
        }
    }

    //Socket Functions
    public void sendToServer(JSONObject jsonObject) {
        System.out.println(appWideCallsService.getCurrentTime() + " --- " + "To Server => " + jsonObject +  "\n");
        socket.emit(KeyStrings.toServer, jsonObject);
    }

    public void loadAddressData()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", "get_state_list");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendToServer(jsonObject);

        try {
            jsonObject.remove("action");
            jsonObject.put("action", "get_city_list");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendToServer(jsonObject);

        try {
            jsonObject.remove("action");
            jsonObject.put("action", "get_station_list");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendToServer(jsonObject);
    }

    private void initConnection() throws InterruptedException, UnsupportedEncodingException
    {
        final ParsePacket parsePacket = new ParsePacket(this);

        try {
            socket = IO.socket("http://" + serverIP + ":" + SERVER_PORT);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                changeConnectionState(true);
                loadAddressData();
                System.out.println(appWideCallsService.getCurrentTime() + " --- " + "Connected");
            }
        }).on(Socket.EVENT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                System.out.println("Event error");
            }
        }).on(Socket.EVENT_RECONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
//                ReconnectToServer();
                System.out.println(appWideCallsService.getCurrentTime() + " --- " + "Reconnected");
            }
        }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
//                setWebSocketHandshakeSuccess(false);
//                showServerConnectionRetryingPanel();
            }
        }).on(KeyStrings.fromServer, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject jsonObject = (JSONObject)args[0];
                parsePacket.parsePacket(jsonObject);
                System.out.println(appWideCallsService.getCurrentTime() + " --- " + "From Server <= " + jsonObject +  "\n");
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                changeConnectionState(false);
            }
        });

        socket.connect();
    }
}
