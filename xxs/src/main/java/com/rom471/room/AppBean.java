package com.rom471.room;

import android.graphics.drawable.Drawable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {
        @Index(value = "id"),
        @Index(value = "pkgname",unique = true)
})
public class AppBean {
    @PrimaryKey(autoGenerate = true)
    private int id;
    String appname;

    String pkgname;
    @Ignore
    Drawable icon;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public AppBean() {
    }

    public String getAppname() {
        return appname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
