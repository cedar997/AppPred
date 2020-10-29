package com.rom471.db;

import android.icu.text.SimpleDateFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Locale;

public class Record {
    int id;
    String appname;
    String pkgname;

    long timeStamp;
    long timeSpend;

    private int battery;
    private int charging;
    private int net;

    public String getPkgname() {
        return pkgname;
    }

    public void setPkgname(String pkgname) {
        this.pkgname = pkgname;
    }

    public long getTimeSpend() {
        return timeSpend;
    }

    public void setTimeSpend(long timeSpend) {
        this.timeSpend = timeSpend;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
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
    public String getChargingString() {

        if(charging==1) return "充电中";
        else if(charging==2) return "USB充电";
        return "未充电";
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
        SimpleDateFormat sdf=new SimpleDateFormat("MM/dd HH:mm:ss", Locale.getDefault());

        return sdf.format(timeStamp);

    }

    public void setDatatime(String datatime) {

    }
}
