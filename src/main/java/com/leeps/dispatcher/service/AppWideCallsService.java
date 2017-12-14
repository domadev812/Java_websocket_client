package com.leeps.dispatcher.service;

import com.leeps.dispatcher.AppFrame;
import com.leeps.dispatcher.common.KeyStrings;
import com.leeps.dispatcher.panels.*;
import com.leeps.dispatcher.uidatamodel.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AppWideCallsService {
    private AppFrame appFrame;

    private OfficerProfilePanel officerProfilePanel;
    private OfficerLocationMapPanel officerLocationMapPanel;
    private OfficerStatusGraphPanel officerStatusGraphPanel;

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

    public void blinkAppIcon(boolean pShouldBlink) {
        appFrame.blinkAppIcon(pShouldBlink);
    }
    public void updatePassword(String newPassword) {appFrame.updatePassword(newPassword);}
    public void showHandledOfficer() {
        appFrame.showHandledOfficer();
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

    public boolean stringIsNullOrEmpty(String pString) {
        if ((pString == null) || (pString.length() < 1)) {
            return true;
        } else {
            return false;
        }
    }

    public BufferedImage resizeImage(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public BufferedImage getEmptyImage() {return appFrame.getEmptyImage();}
    public BufferedImage makeRoundedCorner(BufferedImage image, int cornerRadius) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = output.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);

        g2.dispose();

        return output;
    }

    public JSONObject getDispatcher(){return appFrame.getDispatchObject();}
    public void setDispatchStation(ArrayList<DispatchStationModel> list){
        appFrame.setDispatchStationList(list);
    }

    public JSONObject getHandledOfficer(){return appFrame.getHandledOfficer();}
    public void setHandledOfficer(JSONObject jsonObject) {appFrame.setHandledOfficer(jsonObject);}
    public void setOfficerProfilePanel(OfficerProfilePanel pOfficerProfilePanel) {
        officerProfilePanel = pOfficerProfilePanel;
    }

    public void setOfficerLocationMapPanel(OfficerLocationMapPanel pOfficerLocationMapPanel) {
        officerLocationMapPanel = pOfficerLocationMapPanel;
    }

    public void setOfficerStatusGraphPanel(OfficerStatusGraphPanel pOfficerStatusGraphPanel) {
        officerStatusGraphPanel = pOfficerStatusGraphPanel;
    }

    public void setCurrentOfficerPicBufferedImage(BufferedImage pBufferedImage) {
        officerProfilePanel.setCurrentOfficerPicBufferedImage(pBufferedImage);
    }

    public void showOfficerProfileUiDataModel(JSONObject handledOfficer) {
        officerProfilePanel.showOfficerProfileUiDataModel(handledOfficer);
    }
    public void showLocationMap(boolean flag) {
        officerLocationMapPanel.initLocation(flag);
    }
    public boolean isHandled() {return appFrame.isHandled();}
    public void setHandled(boolean isHandled){appFrame.setHandled(isHandled);}

    public boolean isConnected() {return appFrame.isConnected();}
    public void setConnected(boolean isConnected){appFrame.setConnected(isConnected);}

    public void setLat(double lat){appFrame.setLat(lat);}
    public double getLat(){return appFrame.getLat();}

    public void setLon(double lon){appFrame.setLon(lon);}
    public double getLon(){return appFrame.getLon();}

    public void removeWaitingOfficer(JSONObject jsonObject){
        appFrame.removeWaitingOfficer(jsonObject);
    }

    public int getDispatcherID() {
        return appFrame.getDispatcherID();
    }

    public void initLocationData() {
        officerLocationMapPanel.initLocationData();
    }
    public void initGraphData(JSONObject jsonObject){
        if(jsonObject == null)
        {
            officerStatusGraphPanel.removeAllGraph();
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        long sixtyMinutesAgoMillis = currentTimeMillis - 3600000;
        long lastSixtyMinutesMillis = sixtyMinutesAgoMillis;
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = jsonObject.getJSONArray(KeyStrings.keyValues);
            for (int index = jsonArray.length() - 1; index >= 0; index--) {
                JSONObject stateObject = jsonArray.getJSONObject(index);
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date reportDate = null;
                try {
                    reportDate = format.parse(stateObject.getString(KeyStrings.keyReportTime));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                lastSixtyMinutesMillis = reportDate.getTime();
                officerStatusGraphPanel.addGraphPointHeartRate(
                        lastSixtyMinutesMillis, stateObject.getDouble(KeyStrings.keyHeartRate));

                officerStatusGraphPanel.addGraphPointMotion(
                        lastSixtyMinutesMillis, stateObject.getDouble(KeyStrings.keyMotion));

                officerStatusGraphPanel.addGraphPointPerspiration(
                        lastSixtyMinutesMillis, stateObject.getDouble(KeyStrings.keyPerspiration));

                officerStatusGraphPanel.addGraphPointSkinTemp(
                        lastSixtyMinutesMillis, stateObject.getDouble(KeyStrings.keyTemp));

                officerStatusGraphPanel.addGraphPointOutsideTemp(
                        lastSixtyMinutesMillis, stateObject.getDouble(KeyStrings.keyTemp));

                //lastSixtyMinutesMillis += 60000;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void addGraphData(long reportTime, double heartRate, double motion, double perspiration, double temp)
    {
        officerStatusGraphPanel.addGraphPointHeartRate(
                reportTime, heartRate);

        officerStatusGraphPanel.addGraphPointMotion(
                reportTime, motion);

        officerStatusGraphPanel.addGraphPointPerspiration(
                reportTime, perspiration);

        officerStatusGraphPanel.addGraphPointSkinTemp(
                reportTime, temp);
        officerStatusGraphPanel.addGraphPointOutsideTemp(
                reportTime, temp);
    }
}
