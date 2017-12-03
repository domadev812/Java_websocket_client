package com.leeps.dispatcher.service;

import com.leeps.dispatcher.AppFrame;
import com.leeps.dispatcher.common.KeyStrings;
import com.leeps.dispatcher.uidatamodel.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
//                    case need_assistance:
//                        needAssistance(jsonObject);
//                        break;
//                    case officer_handle_success:
//                        handleOfficerSuccess(jsonObject);
//                        break;
//                    case dispatcher_handled_officer:
//                        anotherDispatcherHandledOfficer(jsonObject);
//                        break;
//                    case already_handled:
//                        appFrame.setHandled(false);
//                        break;
//                    case good_answer:
//                        goodAnswer(jsonObject);
//                        break;
//                    case position_report:
//                        refreshMarker(jsonObject);
//                        break;
//                    case graph_data:
//                        initGraphData(jsonObject);
//                        break;
                }
            }
        });
        ts.start();
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
}
