package com.rom471.db;

import android.icu.text.SimpleDateFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Locale;

public class Record {
    int id;
    String appname;
    long timestamp;
    private int battery;
    private int charging;
    private int net;

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getDatatime() {
        SimpleDateFormat sdf=new SimpleDateFormat("MM月dd日HH时mm分ss秒SSS", Locale.getDefault());

        return sdf.format(timestamp);

    }

    public void setDatatime(String datatime) {

    }
}
