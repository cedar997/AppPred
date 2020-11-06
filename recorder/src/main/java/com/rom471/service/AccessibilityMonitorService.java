package com.rom471.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.accessibility.AccessibilityEvent;

import androidx.room.Room;


import com.rom471.db.RecordDAO;
import com.rom471.db.RecordDataBase;

import java.util.ArrayList;
import java.util.List;

import static android.view.accessibility.AccessibilityEvent.TYPE_VIEW_CLICKED;

public class AccessibilityMonitorService extends AccessibilityService {
    MyRecorder myRecorder;
    public AccessibilityMonitorService() {
    }


    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        myRecorder=new MyRecorder(getApplication());
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int type=event.getEventType();
        myRecorder.record(event);

    }






    
    @Override
    public void onInterrupt() {
        myRecorder.close();
    }
///将会迁移的函数
private RecordDAO getRecordDao(Context context, String db_name){
    RecordDataBase db = Room.databaseBuilder(context, RecordDataBase.class, db_name).allowMainThreadQueries().build();

    return db.getRecordDAO();
}


}