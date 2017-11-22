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
    String keyID = "email";
    String keyEmail = "email";
    String keyPassword = "password";
}
