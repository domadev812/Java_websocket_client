package com.leeps.dispatcher.service;

import com.leeps.dispatcher.AppFrame;
import com.leeps.dispatcher.common.KeyStrings;
import com.leeps.dispatcher.uidatamodel.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ParsePacket {
    AppFrame appFrame;

    public ParsePacket(AppFrame appFrame)
    {
        this.appFrame = appFrame;
    }

    public void parsePacket(final JSONObject jsonObject)
    {
        String strAction = "";

        try {
            strAction = jsonObject.get(KeyStrings.keyAction).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final KeyStrings.DispatcherAction action = KeyStrings.DispatcherAction.valueOf(strAction);

        Thread ts = new Thread(new Runnable() {
            public void run() {
                switch (action)
                {
                    case login:
                        logIn(jsonObject);
                        break;
                    case get_state_list:
                        getStateList(jsonObject);
                        break;
                    case get_city_list:
                        getCityList(jsonObject);
                        break;
                    case get_station_list:
                        getStationList(jsonObject);
                        break;
                    case get_dispatch_station_list:
                        getDispatchStationList(jsonObject);
                        break;
                    case need_assistance:
                        needAssistance(jsonObject);
                        break;
                    case officer_handle_success:
                        handleOfficerSuccess(jsonObject);
                        break;
                    case dispatcher_handled_officer:
                        anotherDispatcherHandledOfficer(jsonObject);
                        break;
                    case already_handled:
                        appFrame.setHandled(false);
                        break;
                    case good_answer:
                        goodAnswer(jsonObject);
                        break;
                    case position_report:
                        updatePosition(jsonObject);
                        break;
                    case graph_data:
                        initGraphData(jsonObject);
                        break;
                }
            }
        });
        ts.start();
    }
    private void initGraphData(JSONObject jsonObject)
    {
        appFrame.initGraphData(jsonObject);
    }
    private void goodAnswer(JSONObject jsonObject)
    {
        String answer = "";
        try {
            answer = jsonObject.getString(KeyStrings.keyAnswer);
            if(answer.equals("no"))
            {
                JSONObject object = new JSONObject();
                object.put(KeyStrings.keyAction, KeyStrings.keyCurrentOfficerHandled);
                object.put(KeyStrings.keyOfficerID, jsonObject.getInt(KeyStrings.keyOfficerID));
                appFrame.setHandledOfficer(null);
                appFrame.setHandled(false);

                appFrame.initGraphData(null);
                appFrame.showOfficerProfileUiDataModel(false);
                appFrame.initLocationData();
                appFrame.sendToServer(object);
            } else{
                JSONObject object = new JSONObject();
                object.put(KeyStrings.keyAction, KeyStrings.keyReportDelayTime);
                object.put(KeyStrings.keyDelayTime, KeyStrings.delay_urgent_time);
                object.put(KeyStrings.keyOfficerID, jsonObject.getInt(KeyStrings.keyOfficerID));
                appFrame.sendToServer(object);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void anotherDispatcherHandledOfficer(JSONObject jsonObject)
    {
        appFrame.removeWaitingOfficer(jsonObject);
    }

    private void updatePosition(JSONObject jsonObject)
    {
        double lat = 0.0, lon = 0.0;
        int officerID = 0;
        String firstName ="", lastName ="";
        try {
            if(!appFrame.isHandled()) return;
            officerID = jsonObject.getInt(KeyStrings.keyOfficerID);
            lat = jsonObject.getDouble(KeyStrings.keyLatitude);
            lon = jsonObject.getDouble(KeyStrings.keyLongitude);
            JSONObject object = appFrame.getHandledOfficer();
            if(object.getInt(KeyStrings.keyID) == officerID)
            {
                appFrame.setLon(lon);
                appFrame.setLat(lat);
                double heartRate = jsonObject.getDouble(KeyStrings.keyHeartRate);
                double motion= jsonObject.getDouble(KeyStrings.keyMotion);
                double perspiration = jsonObject.getDouble(KeyStrings.keyPerspiration);
                double temp = jsonObject.getDouble(KeyStrings.keyTemp);
                String reportTime = jsonObject.getString(KeyStrings.keyReportTime);
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date reportDate = null;
                try {
                    reportDate = format.parse(reportTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                appFrame.showLocationMap(false);
                appFrame.addGraphData(reportDate.getTime(), heartRate, motion, perspiration, temp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void logIn(JSONObject jsonObject) {
        int errorCode = 0;
        JSONObject responseObject = new JSONObject();
        try {
            errorCode = jsonObject.getInt(KeyStrings.keyErrorCode);
            if(errorCode == 0)
                responseObject = jsonObject.getJSONObject("values");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        appFrame.logIn(errorCode, responseObject);
    }

    private void getStateList(JSONObject jsonObject)
    {
        JSONArray jsonArray;
        ArrayList<StateModel> mapState = new ArrayList<StateModel>();
        try {
            jsonArray = jsonObject.getJSONArray(KeyStrings.keyStateList);
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject obj = jsonArray.getJSONObject(i);
                StateModel model = new StateModel();
                model.setId(obj.getInt(KeyStrings.keyID));
                model.setStateName(obj.getString(KeyStrings.keyStateName));
                model.setStateCode(obj.getString(KeyStrings.keyStateCode));
                mapState.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        appFrame.setStateList(mapState);
    }

    private void getStationList(JSONObject jsonObject)
    {
        JSONArray jsonArray;
        ArrayList<StationModel> mapStation = new ArrayList<StationModel>();
        try {
            jsonArray = jsonObject.getJSONArray(KeyStrings.keyStationList);
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject obj = jsonArray.getJSONObject(i);
                StationModel model = new StationModel();
                model.setId(obj.getInt(KeyStrings.keyID));
                model.setCityId(obj.getInt(KeyStrings.keyCityID));
                model.setZipCode(obj.getInt(KeyStrings.keyZipCode));
                model.setStationName(obj.getString(KeyStrings.keyStationName));
                model.setFullAddress(obj.getString(KeyStrings.keyFullAddress));
                model.setStreetName(obj.getString(KeyStrings.keyStreetName));
                model.setPhoneNumber1(obj.getString(KeyStrings.keyPhoneNumber1));
                model.setPhoneNumber2(obj.getString(KeyStrings.keyPhoneNumber2));
                mapStation.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        appFrame.setStationList(mapStation);
    }

    private void getCityList(JSONObject jsonObject){
        JSONArray jsonArray;
        ArrayList<CityModel> mapCity = new ArrayList<CityModel>();
        try {
            jsonArray = jsonObject.getJSONArray(KeyStrings.keyCityList);
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject obj = jsonArray.getJSONObject(i);
                CityModel model = new CityModel();
                model.setId(obj.getInt(KeyStrings.keyID));
                model.setStateId(obj.getInt(KeyStrings.keyStateID));
                model.setCityName(obj.getString(KeyStrings.keyCityName));
                mapCity.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        appFrame.setCityList(mapCity);
    }

    private void getDispatchStationList(JSONObject jsonObject) {
        JSONArray jsonArray;
        ArrayList<DispatchStationModel> mapDispatchStation = new ArrayList<DispatchStationModel>();
        try {
            jsonArray = jsonObject.getJSONArray(KeyStrings.keyDispatchStationList);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                DispatchStationModel model = new DispatchStationModel();

                model.setId(obj.getInt(KeyStrings.keyID));
                model.setDispatch_id(obj.getInt(KeyStrings.keyDispatcherID));
                model.setStation_id(obj.getInt(KeyStrings.keyStationID));
                model.setStation_name(obj.getString(KeyStrings.keyStationName));
                model.setFull_address(obj.getString(KeyStrings.keyFullAddress));
                model.setCity_id(obj.getInt(KeyStrings.keyCityID));
                model.setCity_name(obj.getString(KeyStrings.keyCityName));
                model.setState_id(obj.getInt(KeyStrings.keyStateID));
                model.setState_code(obj.getString(KeyStrings.keyStateCode));
                model.setState_name(obj.getString(KeyStrings.keyStateName));
                model.setType(obj.getInt(KeyStrings.keyType));
                mapDispatchStation.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        appFrame.setDispatchStationList(mapDispatchStation);
    }

    private void needAssistance(JSONObject jsonObject)
    {
        JSONObject object = new JSONObject();
        try {
            object = jsonObject.getJSONObject("officer");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        appFrame.addWaitingOfficer(object);
    }

    private void handleOfficerSuccess(JSONObject jsonObject)
    {
        JSONObject officer = new JSONObject();
        try {
            officer = jsonObject.getJSONObject(KeyStrings.keyOfficer);
            appFrame.setHandledOfficer(officer);
            appFrame.setLat(jsonObject.getDouble(KeyStrings.keyLatitude));
            appFrame.setLon(jsonObject.getDouble(KeyStrings.keyLongitude));
            appFrame.setHandled(true);
            JSONObject object = new JSONObject();
            object.put(KeyStrings.keyAction, KeyStrings.keyGoodAsk);
            object.put(KeyStrings.keyDispatcherID, appFrame.getDispatcherID());
            object.put(KeyStrings.keyOfficerID, officer.getInt(KeyStrings.keyID));
            appFrame.showOfficerProfileUiDataModel(true);
            appFrame.showLocationMap(true);
            appFrame.sendToServer(object);

            JSONObject object1 = new JSONObject();
            object1.put(KeyStrings.keyAction, KeyStrings.keyReportDelayTime);
            object1.put(KeyStrings.keyDelayTime, KeyStrings.delay_urgent_time);
            object1.put(KeyStrings.keyOfficerID, officer.getInt(KeyStrings.keyID));
            appFrame.sendToServer(object1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
