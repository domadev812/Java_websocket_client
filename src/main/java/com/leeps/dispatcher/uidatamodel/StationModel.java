package com.leeps.dispatcher.uidatamodel;

public class StationModel {
    int id, city_id, zip_code;
    String station_name, phone_number1, phone_number2, street_name, full_address;

    public StationModel(){}
    public StationModel(int id, int city_id, int zip_code, String station_name, String phone_number1, String phone_number2, String street_name, String full_address)
    {
        this.id = id;
        this.city_id = city_id;
        this.zip_code = zip_code;
        this.station_name = station_name;
        this.phone_number1 = phone_number1;
        this.phone_number2 = phone_number2;
        this.street_name = street_name;
        this.full_address = full_address;
    }

    public int getId() {return this.id;}
    public void setId(int id){this.id = id;}

    public int getCityId() {return this.city_id;};
    public void setCityId(int city_id){this.city_id = city_id;}

    public int getZipCode() {return this.zip_code;}
    public void setZipCode(int zip_code) {this.zip_code = zip_code;}

    public String getStationName() {return this.station_name;}
    public void setStationName(String station_name) {this.station_name = station_name;}

    public String getPhoneNumber1() {return this.phone_number1;}
    public void setPhoneNumber1(String phone_number1) {this.phone_number1 = phone_number1;}

    public String getPhoneNumber2() {return this.phone_number2;}
    public void setPhoneNumber2(String phone_number2) {this.phone_number2 = phone_number2;}

    public String getStreetName() {return this.street_name;}
    public void setStreetName(String street_name) {this.street_name = street_name;}

    public String getFullAddress() {return this.full_address;}
    public void setFullAddress(String full_address) {this.full_address = full_address;}
}
