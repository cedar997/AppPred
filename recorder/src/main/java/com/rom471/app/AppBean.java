package com.rom471.app;

import android.graphics.drawable.Drawable;

public class AppBean {
    String appname;
    String pkgname;
    Drawable icon;
    long timeSpend;
    public AppBean() {
    }

    public long getTimeSpend() {
        return timeSpend;
    }
    public String getTimeSpendString() {
        long sec=timeSpend/1000;
        if(sec<60)
            return sec+"秒";
        long min=sec/60;
        sec=sec%60;

        return min+"分"+sec+"秒";


    }

    public void setTimeSpend(long timeSpend) {
        this.timeSpend = timeSpend;
    }

    public String getAppname() {
        return appname;
    }

    public String getPkgname() {
        return pkgname;
    }

    public void setPkgname(String pkgname) {
        this.pkgname = pkgname;
    }

    public AppBean(String appname, Drawable icon) {
        this.appname = appname;
        this.icon = icon;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
