package com.rom471.db2;

import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Locale;

@Entity(indices = {@Index(value = {"pkgName"},
        unique = true)})
public class App {
    @PrimaryKey (autoGenerate = true)
    long appId;
    //应用名
    String appName;
    //包名
    String pkgName;
    //应用图标
    @Ignore
    Drawable icon;
    //应用第一次运行时间
    long firstRunningTime;
    //应用最后一次运行时间
    long lastRuningTime;
    //应用总使用时间
    long totalRuningTime;
    //使用次数
    long useCount;

    public long getUseCount() {
        return useCount;
    }

    public void setUseCount(long useCount) {
        this.useCount = useCount;
    }
    public void addUseCount(){
        this.useCount++;
    }
    public long getFirstRunningTime() {
        return firstRunningTime;
    }

    public void setFirstRunningTime(long firstRunningTime) {
        this.firstRunningTime = firstRunningTime;
    }

    public long getLastRuningTime() {
        return lastRuningTime;
    }

    public void setLastRuningTime(long lastRuningTime) {
        this.lastRuningTime = lastRuningTime;
    }

    public long getTotalRuningTime() {
        return totalRuningTime;
    }

    public void setTotalRuningTime(long totalRuningTime) {
        this.totalRuningTime = totalRuningTime;
    }

    public void addTotalRuningTime(long timespend){
        this.totalRuningTime+=timespend;
    }
    public App() {
    }

    public long getAppId() {
        return appId;
    }

    public void setAppId(long appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "App{" +
                "appName='" + appName + '\'' +
                ", lastRuningTime=" + lastRuningTime +
                ", totalRuningTime=" + totalRuningTime +
                '}';
    }


}
