package com.leeps.dispatcher.service;

import com.leeps.dispatcher.AppFrame;
import org.json.JSONObject;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AppWideCallsService {
    private AppFrame appFrame;

    public AppWideCallsService() {
    }

    public void setAppFrame(AppFrame pAppFrame) {
        appFrame = pAppFrame;
    }

    public String getCurrentTime(){
        Date date = new Date();
        String currentTime = "";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        currentTime = dateFormat.format(date);
        return currentTime;
    }

    public void sendToServer(JSONObject jsonObject) {
        appFrame.sendToServer(jsonObject);
    }

    public Rectangle positionAndSize27inchWideMonitor() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double currentScreenWidth = screenSize.getWidth();
        double currentScreenHeight = screenSize.getHeight();

        int makeWidth = (int) (currentScreenWidth * 0.9);
        int makeHeight = (int) (currentScreenHeight * 0.9);

        int makeXLocation = (int) (currentScreenWidth * 0.05);
        int makeYLocation = (int) (currentScreenHeight * 0.04);

        Rectangle preferredAppLocationAndSize = new Rectangle(
                makeXLocation, makeYLocation, makeWidth, makeHeight);

        return preferredAppLocationAndSize;
    }

    public Rectangle positionAndSize24inchWideMonitor() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double currentScreenWidth = screenSize.getWidth();
        double currentScreenHeight = screenSize.getHeight();

        int makeWidth = (int) (currentScreenWidth * 0.8);
        int makeHeight = (int) (currentScreenHeight * 0.8);

        int makeXLocation = (int) (currentScreenWidth * 0.10);
        int makeYLocation = (int) (currentScreenHeight * 0.09);

        Rectangle preferredAppLocationAndSize = new Rectangle(
                makeXLocation, makeYLocation, makeWidth, makeHeight);

        return preferredAppLocationAndSize;
    }
}
