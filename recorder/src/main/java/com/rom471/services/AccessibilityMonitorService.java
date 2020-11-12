package com.rom471.services;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

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