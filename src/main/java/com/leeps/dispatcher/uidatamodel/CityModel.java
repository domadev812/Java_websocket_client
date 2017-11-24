package com.leeps.dispatcher.uidatamodel;

public class CityModel {
    int id, state_id;
    String city_name;

    public CityModel(){}
    public CityModel(int id, int state_id, String city_name)
    {
        this.id = id;
        this.state_id = state_id;
        this.city_name = city_name;
    }

    public int getId() {return this.id;}
    public void setId(int id) {this.id = id;}

    public int getStateId() {return this.state_id;}
    public void setStateId(int state_id) {this.state_id = state_id;}

    public String getCityName() {return this.city_name;}
    public void setCityName(String city_name) {this.city_name = city_name;}
}
