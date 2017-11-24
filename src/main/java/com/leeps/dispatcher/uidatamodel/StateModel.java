package com.leeps.dispatcher.uidatamodel;

public class StateModel {
    int id;
    String state_name, state_code;

    public StateModel(){}
    public StateModel(int id, String state_name, String state_code)
    {
        this.id = id;
        this.state_name = state_name;
        this.state_code = state_code;
    }

    public int getId() {return this.id;}
    public void setId(int id) {this.id = id;}

    public String getStateName() {return this.state_name;}
    public void setStateName(String state_name) {this.state_name = state_name;}

    public String getStateCode() {return this.state_code;}
    public void setStateCode(String state_code) {this.state_code = state_code;}
}
