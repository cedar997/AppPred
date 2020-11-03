package com.rom471.room;

import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Locale;
@Entity
public class Record implements Serializable {
    @Ignore
    private static final long serialVersionUID = 1L;
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String appname;
    private String pkgname;
    private long timeStamp;
    private long timeSpend;
    private int battery;
    private int charging;
    private int net;
    @Ignore
    private Drawable icon;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public Record() {
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

    public String getPkgname() {
        return pkgname;
    }

    public void setPkgname(String pkgname) {
        this.pkgname = pkgname;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getTimeSpend() {
        return timeSpend;
    }

    public void setTimeSpend(long timeSpend) {
        this.timeSpend = timeSpend;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public int getCharging() {
        return charging;
    }

    public void setCharging(int charging) {
        this.charging = charging;
    }

    public int getNet() {
        return net;
    }

    public void setNet(int net) {
        this.net = net;
    }

    //---------------------------
    public String getNetString(){

        if(net==1) return "移动网络";
        else if(net==2) return "wifi";
        else return "无网络";
    }

    public String getChargingString() {

        if(charging==1) return "充电中";
        else if(charging==2) return "USB充电";
        return "未充电";
    }
    public String getTimeSpendString() {
        long sec=timeSpend/1000;
        if(sec<60)
            return sec+"秒";
        long min=sec/60;
        sec=sec%60;

        return min+"分"+sec+"秒";


    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getDatatime() {
        SimpleDateFormat sdf=new SimpleDateFormat("MM/dd HH:mm:ss", Locale.getDefault());

        return sdf.format(timeStamp);

    }


}
