package com.rom471.recorder;

import android.accessibilityservice.AccessibilityService;
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
    public AccessibilityMonitorService() {
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        db=new RecordDBHelper(getApplicationContext(),"app.db");
        record=new Record();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int type=event.getEventType();
        switch (type){
            //窗口变化时触发

            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                mWindowClassName = event.getClassName();
                String package_str= event.getPackageName()==null?"":event.getPackageName().toString();
                String appLable=null;
                PackageManager pm = getPackageManager();
                try {
                    appLable = pm.getPackageInfo(package_str,PackageManager.GET_ACTIVITIES).applicationInfo.loadLabel(pm).toString();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                if(appLable!="" && !last.equals(appLable)) {
                    record.setAppname(last);
                    DBUtils.storeBatteryInfo(getApplicationContext(),record);
                    DBUtils.storeNetworkInfo(getApplicationContext(),record);
                    db.insertRecord(record);
                    record.setAppname(appLable);
                    db.insertRecord(record);
                    last =appLable;
                }
                break;
            case TYPE_VIEW_CLICKED:
            case TYPE_VIEW_LONG_CLICKED:
                break;
        }
    }

    @Override
    public void onInterrupt() {

    }



}