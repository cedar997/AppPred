package com.rom471.db;

public class Record {
    int id;
    String appname;
    String datatime;
    private int battery;
    private int charging;
    private int net;

    public int getNet() {
        return net;
    }
    public String getNetString(){

        if(net==1) return "移动网络";
        else if(net==2) return "wifi";
        else return "无网络";
    }
    public void setNet(int net) {
        this.net = net;
    }

    public int getBattery() {
        return battery;
    }

    public int getCharging() {
        return charging;
    }
    public String getChargingString(){
        return ""+charging/1000+" mW";
    }
    public void setCharging(int charging) {
        this.charging = charging;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getDatatime() {
        return datatime;
    }

    public void setDatatime(String datatime) {
        this.datatime = datatime;
    }
}
