package com.leeps.dispatcher.common;

public interface KeyStrings {
    String toServer = "from_dispatcher";
    String fromServer = "server_to_dispatcher";

    enum DispatcherAction {
        login, get_state_list, get_city_list, get_station_list, get_dispatch_station_list, deactive_profile, need_assistance, officer_handle_success, good_answer, dispatcher_handled_officer, current_officer_handled, start_assist, already_handled, position_report, graph_data
    }

    String keyAction = "action";

    // Action Value
    String actionLogin = "login";

    //JSON Key
    String keyErrorCode = "errorCode";
    String keyID = "id";
    String keyEmail = "email";
    String keyPassword = "password";
    String keyStateList = "state_list";
    String keyStateName = "state_name";
    String keyStateCode = "state_code";
    String keyStateID = "state_id";

    String keyCityList = "city_list";
    String keyCityID = "city_id";
    String keyCityName = "city_name";
    String keyDispatchStationList = "dispatch_station_list";
    String keyOfficerID = "officerId";
    String keyDispatcherID = "dispatcherId";
    String keyZipCode = "zip_code";
    String keyStationName = "station_name";
    String keyFullAddress = "full_address";
    String keyStreetName = "street_name";
    String keyPhoneNumber1 = "phone_number1";
    String keyPhoneNumber2 = "phone_number2";
    String keyStationList = "station_list";
    String keyStationID = "station_id";
    String keyType = "type";
}
