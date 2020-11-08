package com.rom471.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.accessibility.AccessibilityEvent;

import androidx.room.Room;



import java.util.ArrayList;
import java.util.List;

import static android.view.accessibility.AccessibilityEvent.TYPE_VIEW_CLICKED;

public class AccessibilityMonitorService extends AccessibilityService {

    MyRecorder2 myRecorder2;
    public AccessibilityMonitorService() {
    }


    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();


        myRecorder2=new MyRecorder2(getApplication());
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int type=event.getEventType();

        myRecorder2.record(event);

    }






    
    @Override
    public void onInterrupt() {
        myRecorder2.close();
    }
///将会迁移的函数



}