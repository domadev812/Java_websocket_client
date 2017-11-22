package com.leeps.dispatcher;

import com.leeps.dispatcher.common.KeyStrings;
import com.leeps.dispatcher.dialogs.LoginDialog;
import com.leeps.dispatcher.service.AppWideCallsService;
import com.leeps.dispatcher.service.ParsePacket;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

public class AppFrame extends JFrame {
    private AppFrame thisAppFrame;

    // Dispatcher Profile Variable
    int dispatcherID;

    //Service and Constant Class Variable
    private AppWideCallsService appWideCallsService;

    //UI Component
    private LoginDialog loginDialog;
    private JSONObject dispatcherProfile = new JSONObject();

    //Socket Variables
    Socket socket;
    final private String serverIP = "192.168.0.100";
//    final private String serverIP = "ec2-user@ec2-34-213-184-150.us-west-2.compute.amazonaws.com";
    final private int SERVER_PORT = 8120;

    public AppFrame() {
        thisAppFrame = this;

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

        loginDialog = new LoginDialog(640, 390, appWideCallsService);
        loginDialog.setVisible(true);

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // UI Component Build Functions

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
        } else {
            loginDialog.showErrorMessage("E-Mail or Password is incorrect.");
        }
    }

    //Socket Functions
    public void sendToServer(JSONObject jsonObject) {
        System.out.println(appWideCallsService.getCurrentTime() + " --- " + "To Server => " + jsonObject +  "\n");
        socket.emit(KeyStrings.toServer, jsonObject);
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
//                loadAddressData();
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
