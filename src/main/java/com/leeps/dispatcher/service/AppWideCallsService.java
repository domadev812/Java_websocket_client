package com.leeps.dispatcher.service;

import com.leeps.dispatcher.AppFrame;
import org.json.JSONObject;

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
}
