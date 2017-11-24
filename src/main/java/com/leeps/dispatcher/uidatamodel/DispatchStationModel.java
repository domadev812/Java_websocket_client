package com.leeps.dispatcher.uidatamodel;

public class DispatchStationModel {
    int id, dispatch_id, station_id, city_id, state_id, type;
    String full_address, station_name, city_name, state_code, state_name;

    public DispatchStationModel() {}

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public void setCity_id(int city_id) {this.city_id = city_id;}
    public int getCity_id() {return city_id;}

    public void setDispatch_id(int dispatch_id) {this.dispatch_id = dispatch_id;}
    public int getDispatch_id() {return dispatch_id;}

    public void setState_id(int state_id) {this.state_id = state_id;}
    public int getState_id() {return state_id;}

    public void setType(int type) {this.type = type;}
    public int getType() {return this.type;}

    public void setStation_id(int station_id) {this.station_id = station_id;}
    public int getStation_id() {return station_id;}

    public void setCity_name(String city_name) {this.city_name = city_name;}
    public String getCity_name() {return city_name;}

    public void setFull_address(String full_address) {this.full_address = full_address;}
    public String getFull_address() {return full_address;}

    public void setState_code(String state_code) {this.state_code = state_code;}
    public String getState_code() {return state_code;}

    public void setState_name(String state_name) {this.state_name = state_name;}
    public String getState_name() {return state_name;}

    public void setStation_name(String station_name) {this.station_name = station_name;}
    public String getStation_name() {return station_name;}
}
