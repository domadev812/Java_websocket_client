package com.leeps.dispatcher.service;

import com.leeps.dispatcher.AppFrame;
import com.leeps.dispatcher.uidatamodel.*;
import org.json.JSONObject;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    public ArrayList<StateModel> getStateList() {return appFrame.getStateList();}

    public ArrayList<CityModel> getCityList(int stateID) {
        ArrayList<CityModel> listCity = appFrame.getCityList();
        ArrayList<CityModel> filterCity = new ArrayList<CityModel>();
        for (int i = 0; i < listCity.size(); i++)
        {
            CityModel model = listCity.get(i);
            if(model.getStateId() == stateID)
                filterCity.add(model);
        }
        return filterCity;
    }

    public ArrayList<StationModel> getStationList(int cityID) {
        ArrayList<StationModel> listStation = appFrame.getStationList();
        ArrayList<StationModel> filterStation = new ArrayList<StationModel>();
        if(cityID == 0) return listStation;
        for (int i = 0; i < listStation.size(); i++)
        {
            StationModel model = listStation.get(i);
            if(model.getCityId() == cityID)
                filterStation.add(model);
        }
        return filterStation;
    }

    public ArrayList<DispatchStationModel> getDispatchStationList(int type)
    {
        ArrayList<DispatchStationModel> list = new ArrayList<DispatchStationModel>();
        ArrayList<DispatchStationModel> dispatchStationModels = appFrame.getDispatchStationList();
        for(int i = 0; i < dispatchStationModels.size(); i++)
        {
            DispatchStationModel model = dispatchStationModels.get(i);
            if(model.getType() == type)
                list.add(model);
        }
        return list;
    }
}
