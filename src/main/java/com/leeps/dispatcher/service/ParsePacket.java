package com.leeps.dispatcher.service;

import com.leeps.dispatcher.AppFrame;
import com.leeps.dispatcher.common.KeyStrings;
import org.json.JSONException;
import org.json.JSONObject;

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
//                    case get_state_list:
//                        getStateList(jsonObject);
//                        break;
//                    case get_city_list:
//                        getCityList(jsonObject);
//                        break;
//                    case get_station_list:
//                        getStationList(jsonObject);
//                        break;
//                    case get_dispatch_station_list:
//                        getDispatchStationList(jsonObject);
//                        break;
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
}
