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
    String keyReconnect = "reconnect";
    String keyChangePassword = "change_dispatcher_password";
    //JSON Key
    String keyErrorCode = "errorCode";

    String keyID = "id";
    String keyEmail = "user_email";
    String keyFirstName = "first_name";
    String keyLastName = "last_name";
    String keyUserID = "user_id";
    String keyPassword = "password";
    String keyUserPassword = "user_password";
    String keyStateList = "state_list";
    String keyStateName = "state_name";
    String keyStateCode = "state_code";
    String keyStateID = "state_id";

    String keyCityList = "city_list";
    String keyCityID = "city_id";
    String keyCityName = "city_name";
    String keyDispatchStationList = "dispatch_station_list";
    String keySetDispatchStation = "set_dispatch_station_list";
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
    String keyValues = "values";

    String keyDispatchActive = "dispatch_active";
    String keyActiveFlag = "active_dispatcher_flag";

    String keyPhoneNumber = "phone_number";
    String keyRank = "rank";
    String keyGender = "gender";
    String keyRace = "race";
    String keyHeight = "height";
    String keyBloodType = "blood_type";
    String keyAllergy = "allergy";
    String keyContactPhone = "contact_number";
    String keyContactRelation = "contact_relation";
    String keyWeight = "weight";
    String keyBadgeNumber = "badge_number";
    String keyLatitude = "lat";
    String keyLongitude = "lon";
    String keyOfficerHandle = "officer_handle";
    String keyOfficer = "officer";

    String keyReportDelayTime = "report_delay_time";
    String keyDelayTime = "delay_time";
    String keyGoodAsk = "good_ask";
    String keyAnswer = "answer";
    public int delay_urgent_time = 10;
    String keyCurrentOfficerHandled = "current_officer_handled";

    public String keyHeartRate = "heartRate";
    public String keyMotion = "motion";
    public String keyPerspiration = "perspiration";
    public String keyTemp = "temp";
    public String keyReportTime = "reportTime";
}
