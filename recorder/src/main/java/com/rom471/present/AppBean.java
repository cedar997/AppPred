package com.rom471.present;

import android.graphics.drawable.Drawable;

public class AppBean {
    String appname;
    String pkgname;
    Drawable icon;

    public AppBean() {
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
