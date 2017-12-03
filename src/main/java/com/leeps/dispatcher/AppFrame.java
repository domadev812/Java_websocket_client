package com.leeps.dispatcher;

import com.leeps.dispatcher.common.*;
import com.leeps.dispatcher.dialogs.*;
import com.leeps.dispatcher.material.MaterialButton;
import com.leeps.dispatcher.service.*;
import com.leeps.dispatcher.uidatamodel.*;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import oracle.jvm.hotspot.jfr.JFR;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
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
    private ImageIcon offlinedImageIcon;
    private enum WhichAppIcon {
        APP_ICON_1, APP_ICON_2
    }

    //Menu Component
    private JMenuBar appMenuBar;
    private JMenu dispatcherProfileMenu;
    private JMenu windowMenu;
    private JMenu helpMenu;
    private MaterialButton alarmsPendingButton;

    //UI Component
    private LoginDialog loginDialog;
    private DispatcherProfileDialog dispatcherProfileDialog = null;

    private JPanel contentPanel;
    private JPanel centerPanel;
    private JPanel bottomPanel;

    private JLabel lblApplicationStatus = new JLabel();
    private JLabel lblConnection = new JLabel();
    private JLabel lblConnectionImage = new JLabel();

    private Rectangle preferredAppLocationAndSize;

    private ArrayList<StateModel> listState = new ArrayList<StateModel>();
    private ArrayList<CityModel> listCity = new ArrayList<CityModel>();
    private ArrayList<StationModel> listStation = new ArrayList<StationModel>();
    private ArrayList<DispatchStationModel> listDispatcherStation = new ArrayList<DispatchStationModel>();

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

        loginDialog = new LoginDialog(this, 640, 390, appWideCallsService);
        loginDialog.setVisible(true);

        initCustomizedUiWidgetsFactory();
        initProperties();
        layoutUI();

        setContentPane(contentPanel);
//        addWindowListener(new FrameWindowListener());
//        addComponentListener(new FrameResizedListener());

        setJMenuBar(appMenuBar);
        setTitle(AppWideStrings.appTitle);
        makeAppPreferredAppLocationAndSize();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
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
    // UI Effect Service Functions
    private void initCustomizedUiWidgetsFactory() {
        customizedUiWidgetsFactory = new CustomizedUiWidgetsFactory();
        genericUserImageIcon = customizedUiWidgetsFactory.makeImageIcon(
                AppWideStrings.icon_generic_user_green);
        connectedImageIcon = new ImageIcon(getClass().getResource(AppWideStrings.connectedImage));
        offlinedImageIcon = new ImageIcon(getClass().getResource(AppWideStrings.offlineImage));
    }

    private void applyMenuEffect(final JMenu pJMenu) {
        pJMenu.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        pJMenu.setCursor(handPointingCursor);
        pJMenu.setForeground(Color.WHITE);
        pJMenu.setFont(common.getRobotoBoldFont(14.0f));
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
        windowMenu = new JMenu(
                AppWideStrings.menuBarWindowString);
        helpMenu = new JMenu(AppWideStrings.menuBarHelpString);

        customizedUiWidgetsFactory.removeAllMouseListeners(dispatcherProfileMenu);
        customizedUiWidgetsFactory.removeAllMouseListeners(windowMenu);
        customizedUiWidgetsFactory.removeAllMouseListeners(helpMenu);

        applyMenuEffect(dispatcherProfileMenu);
        applyMenuEffect(windowMenu);
        applyMenuEffect(helpMenu);

        handPointingCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

        alarmsPendingButton = new MaterialButton(AppWideStrings.alarmPendingButtonString + 0, new Color(0x4F, 0x4F, 0x4F), Color.WHITE, new Color(0x56, 0x56, 0x56));
        alarmsPendingButton.setFont(common.getRobotoBoldFont(14.0f));
        alarmsPendingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent pE) {
//                alarmsPendingDialog.setVisible(true);
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

        dispatcherProfileMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent pE) {
                System.out.println("Visible Dialog");
                dispatcherProfileDialog = new DispatcherProfileDialog(thisAppFrame,1120, 800, appWideCallsService);
//                dispatcherProfileDialog.setVisible(true);
            }
        });

        windowMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent pE) {
//                showMultiPanel();
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
//        contentPanel.add(centerPanel, BorderLayout.CENTER);
        // contentPaneJPanel.add(topBarPanel, BorderLayout.NORTH);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);
    }
    // UI Component Choose Functions
    private void showHandledOfficer() {

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
            showHandledOfficer();
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
//                setWebSocketHandshakeSuccess(true);
//                showServerConnectionUpPanel();
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
                System.out.println(appWideCallsService.getCurrentTime() + " --- " + "Disconnected");
            }
        });

        socket.connect();
    }
}
