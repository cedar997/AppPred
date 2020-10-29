package com.rom471.recorder;

import android.accessibilityservice.AccessibilityService;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.rom471.db.DBUtils;
import com.rom471.db.Record;
import com.rom471.db.RecordDBHelper;

import java.util.ArrayList;
import java.util.List;

import static android.view.accessibility.AccessibilityEvent.TYPES_ALL_MASK;
import static android.view.accessibility.AccessibilityEvent.TYPE_TOUCH_INTERACTION_END;
import static android.view.accessibility.AccessibilityEvent.TYPE_TOUCH_INTERACTION_START;
import static android.view.accessibility.AccessibilityEvent.TYPE_VIEW_CLICKED;
import static android.view.accessibility.AccessibilityEvent.TYPE_VIEW_FOCUSED;
import static android.view.accessibility.AccessibilityEvent.TYPE_VIEW_LONG_CLICKED;
import static android.view.accessibility.AccessibilityEvent.TYPE_VIEW_SCROLLED;

public class AccessibilityMonitorService extends AccessibilityService {
    private CharSequence mWindowClassName;
    private String last ="";
    RecordDBHelper db;
    Record record;
    PackageManager pm;
    List<String> exclude_names;
    List<String> skip_names;
    boolean filter_exclude=false;
    boolean filter_skip=false;
    public AccessibilityMonitorService() {
    }

    public List<String> getSkip_names() {
        List<String> excludes=new ArrayList<>();
        excludes.add("搜狗输入法小米版");
        excludes.add("系统 UI");
        excludes.add("系统桌面");
        return excludes;
    }

    private List<String> getExclude_names(){
        List<String> excludes=new ArrayList<>();
        excludes.add("Recorder");
        excludes.add("系统桌面");
        excludes.add(("万象息屏"));
        excludes.add("小米画报");
        excludes.add("安卓系统");
        excludes.add("系统 UI");
        excludes.add("手机管家");
        excludes.add("Android 系统");

        return excludes;
    }
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        db=new RecordDBHelper(getApplicationContext(),"app.db");
        record=new Record();
        pm = getPackageManager();//初始化包管理器
        exclude_names=getExclude_names();//初始化过滤应用名列表
        skip_names=getSkip_names();
        filter_exclude=true;//过滤开关
        filter_skip=true;//过滤开关
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int type=event.getEventType();
        record(event);

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
    //开始记录
    private void record_start(String name){
        record=new Record();
        record.setAppname(name);
        long l = System.currentTimeMillis();
        record.setTimeStamp(l);

    }
    //结束记录
    private void record_finish(String name){
        if(record.getAppname().equals(name)){
            long l = System.currentTimeMillis();
            long spend=l-record.getTimeStamp();
            record.setTimeSpend(spend);
            DBUtils.storeBatteryInfo(getApplicationContext(),record);
            DBUtils.storeNetworkInfo(getApplicationContext(),record);
            db.insertRecord(record);
        }
        else {
            record_start(name);
        }
    }
    private void record(AccessibilityEvent event){
        String appLabel = getAppLabel(event) ;
        switch (event.getEventType()){
            //窗口变化时触发

            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED: //后台弹出也会触发，用skip
                if(filter_skip&& skip_names.contains(appLabel))return;
                if(filter_exclude && exclude_names.contains(appLabel)){
                    if(last!="")
                        record_finish(last);
                    last="";
                    return;

                }
                //Log.d("cedar", "skip "+appLabel);
                if(appLabel.equals("") || appLabel==null) return;

                if(appLabel.equals(last)){

                }else{
                    if(!last.equals(""))
                        record_finish(last);
                    record_start(appLabel);
                    last=appLabel;
                }

                break;

            //case TYPE_VIEW_CLICKED:
            //case TYPE_TOUCH_INTERACTION_START: //no
            case TYPE_VIEW_CLICKED: //点击时才触发,滑动没用，用exclude
                if(filter_exclude&& exclude_names.contains(appLabel)) {
                    if(last!="")
                        record_finish(last);
                    last="";
                    return;
                }
                //Log.d("cedar", "exclude "+appLabel);
                if(appLabel.equals("") || appLabel==null) return;

                if(appLabel.equals(last)){

                }else{
                    if(!last.equals(""))
                        record_finish(last);
                    record_start(appLabel);
                    last=appLabel;
                }
                break;


        }



    }
    
    @Override
    public void onInterrupt() {

    }



}