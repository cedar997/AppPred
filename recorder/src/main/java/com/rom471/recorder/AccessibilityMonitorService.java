package com.rom471.recorder;

import android.accessibilityservice.AccessibilityService;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.view.accessibility.AccessibilityEvent;

import com.rom471.db.DBUtils;
import com.rom471.db.Record;
import com.rom471.db.RecordDBHelper;

import static android.view.accessibility.AccessibilityEvent.TYPE_VIEW_CLICKED;
import static android.view.accessibility.AccessibilityEvent.TYPE_VIEW_LONG_CLICKED;

public class AccessibilityMonitorService extends AccessibilityService {
    private CharSequence mWindowClassName;
    private String last ="";
    RecordDBHelper db;
    Record record;
    PackageManager pm;
    public AccessibilityMonitorService() {
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        db=new RecordDBHelper(getApplicationContext(),"app.db");
        record=new Record();
        pm = getPackageManager();//初始化包管理器
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int type=event.getEventType();
        switch (type){
            //窗口变化时触发

            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                record(event);
                break;
            case TYPE_VIEW_CLICKED:
                record(event);
                break;
            case TYPE_VIEW_LONG_CLICKED:
                break;
        }
    }
    private void saveRecord(String app_name){
        record.setAppname(app_name);
        DBUtils.storeBatteryInfo(getApplicationContext(),record);
        DBUtils.storeNetworkInfo(getApplicationContext(),record);
        db.insertRecord(record);
    }
    //获得应用名称
    private String getAppLabel(AccessibilityEvent event){
        String package_str= event.getPackageName()==null?"":event.getPackageName().toString();
        String appLabel="";
        try {
            appLabel = pm.getPackageInfo(package_str,PackageManager.GET_ACTIVITIES).applicationInfo.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appLabel;

    }
    private void record(AccessibilityEvent event){
        String appLabel = getAppLabel(event);
        if(appLabel.equals("")) return;
        if(appLabel.equals(last)){

        }else{
            if(!last.equals(""))
                saveRecord(last);
            saveRecord(appLabel);
            last=appLabel;
        }

    }
    
    @Override
    public void onInterrupt() {

    }



}