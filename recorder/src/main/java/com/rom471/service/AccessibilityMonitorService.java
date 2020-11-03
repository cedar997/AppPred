package com.rom471.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.accessibility.AccessibilityEvent;

import androidx.room.Room;

import com.rom471.utils.DBUtils;
import com.rom471.db.Record;
import com.rom471.db.RecordDAO;
import com.rom471.db.RecordDataBase;

import java.util.ArrayList;
import java.util.List;

import static android.view.accessibility.AccessibilityEvent.TYPE_VIEW_CLICKED;

public class AccessibilityMonitorService extends AccessibilityService {
    private CharSequence mWindowClassName;
    private String lastPkgname ="";
    RecordDAO dao;
    Record record;
    PackageManager pm;
    List<String> exclude_names;
    List<String> skip_names;
    boolean filter_exclude=false;
    boolean filter_skip=false;
    RecordDataBase recordDB;
    public AccessibilityMonitorService() {
    }

    public List<String> getSkip_names() {
        List<String> excludes=new ArrayList<>();
        excludes.add("搜狗输入法小米版");
        excludes.add("系统 UI");
        excludes.add("系统桌面");
        excludes.add("智能服务");
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
        excludes.add("权限管理服务");

        return excludes;
    }
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        //room数据库
        recordDB = Room.databaseBuilder(getApplicationContext(), RecordDataBase.class,"records.db").allowMainThreadQueries().build();
        dao=recordDB.getRecordDAO();
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
    private String getAppLabel(String pkgname){
        String appLabel="";
        try {
            appLabel = pm.getPackageInfo(pkgname,PackageManager.GET_ACTIVITIES).applicationInfo.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appLabel;

    }
    //开始记录
    private void record_start(String pkgname){
        record=new Record();
        record.setPkgname(pkgname);
        record.setAppname(getAppLabel(pkgname));
        long l = System.currentTimeMillis();
        record.setTimeStamp(l);

    }
    //结束记录
    private void record_finish(String pkgname){
        if(record.getPkgname().equals(pkgname)){
            long l = System.currentTimeMillis();
            long spend=l-record.getTimeStamp();
            record.setAppname(getAppLabel(pkgname));
            record.setTimeSpend(spend);
            DBUtils.storeBatteryInfo(getApplicationContext(),record);
            DBUtils.storeNetworkInfo(getApplicationContext(),record);

            dao.insertRecord(record);
        }
        else {
            record_start(pkgname);
        }
    }
    private void record(AccessibilityEvent event){
        String pkgname= event.getPackageName()==null?"":event.getPackageName().toString();
        String appLabel= getAppLabel(pkgname);
//        String appLabel = getAppLabel(event) ;
        switch (event.getEventType()){
            //窗口变化时触发

            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED: //后台弹出也会触发，用skip
                if(filter_skip&& skip_names.contains(appLabel))return;
                if(filter_exclude && exclude_names.contains(appLabel)){
                    if(lastPkgname !="")
                        record_finish(lastPkgname);
                    lastPkgname ="";
                    return;

                }
                //Log.d("cedar", "skip "+appLabel);
                if(appLabel.equals("") || appLabel==null) return;

                if(pkgname.equals(lastPkgname)){

                }else{
                    if(!lastPkgname.equals(""))
                        record_finish(lastPkgname);
                    record_start(pkgname);
                    lastPkgname =pkgname;
                }

                break;

            //case TYPE_VIEW_CLICKED:
            //case TYPE_TOUCH_INTERACTION_START: //no
            case TYPE_VIEW_CLICKED: //点击时才触发,滑动没用，用exclude
                if(filter_exclude&& exclude_names.contains(appLabel)) {
                    if(lastPkgname !="")
                        record_finish(lastPkgname);
                    lastPkgname ="";
                    return;
                }
                //Log.d("cedar", "exclude "+appLabel);
                if(appLabel.equals("") || appLabel==null) return;

                if(pkgname.equals(lastPkgname)){

                }else{
                    if(!lastPkgname.equals(""))
                        record_finish(lastPkgname);
                    record_start(pkgname);
                    lastPkgname =pkgname;
                }
                break;


        }



    }
    
    @Override
    public void onInterrupt() {
        recordDB.close();
    }
///将会迁移的函数
private RecordDAO getRecordDao(Context context, String db_name){
    RecordDataBase db = Room.databaseBuilder(context, RecordDataBase.class, db_name).allowMainThreadQueries().build();

    return db.getRecordDAO();
}


}