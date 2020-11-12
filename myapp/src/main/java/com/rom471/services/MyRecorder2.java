package com.rom471.services;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.rom471.db2.App;
import com.rom471.db2.AppDao;
import com.rom471.db2.AppDataBase;
import com.rom471.db2.Event;
import com.rom471.db2.OnePred;
import com.rom471.db2.OneUse;
import com.rom471.db2.SimpleApp;
import com.rom471.pred.MyPredicter2;
import com.rom471.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;

import static android.view.accessibility.AccessibilityEvent.TYPE_VIEW_CLICKED;

public class MyRecorder2 {
    private String lastPkgname = "";
   // RecordDAO dao;

    AppDao appDao;
    App app;
    OneUse oneUse;
    Context context;
    PackageManager pm;
    SimpleApp last_simple_app;
    List<String> exclude_names;
    List<String> skip_names;
    boolean filter_exclude = false;
    boolean filter_skip = false;
    boolean app_first=false;//当前app是否是第一次插入
    boolean record_events=false;
    AppDataBase appDB;
    MyPredicter2 pedictor;
    public MyRecorder2(Context context) {
        this.context=context;
        appDB =AppDataBase.getInstance(context);
        appDao= appDB.getAppDao();
        pm = context.getPackageManager();//初始化包管理器
        exclude_names = getExclude_names();//初始化过滤应用名列表
        skip_names = getSkip_names();
        filter_exclude = true;//过滤开关
        filter_skip = true;//过滤开关
        record_events=true;
        pedictor=MyPredicter2.getInstance((Application) context.getApplicationContext());

    }

    public List<String> getSkip_names() {
        List<String> excludes = new ArrayList<>();
        excludes.add("搜狗输入法小米版");
        excludes.add("系统 UI");

        excludes.add("智能服务");
        return excludes;
    }

    private List<String> getExclude_names() {
        List<String> excludes = new ArrayList<>();
        excludes.add("Recorder");
        excludes.add("系统桌面");
        excludes.add(("万象息屏"));
        excludes.add("小米画报");
        excludes.add("安卓系统");
        excludes.add("系统 UI");
        excludes.add("手机管家");
        excludes.add("Android 系统");
        excludes.add("权限管理服务");
        excludes.add("用户行为");
        excludes.add("用户行为3");
        excludes.add("应用预测");
        excludes.add("智能服务");

        excludes.add("搜狗输入法小米版");
        excludes.add("设置");
        return excludes;
    }

    //获得应用名称
    private String getAppLabel(String pkgname) {
        String appLabel = "";
        try {
            appLabel = pm.getPackageInfo(pkgname, PackageManager.GET_ACTIVITIES).applicationInfo.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appLabel;

    }

    //开始记录
    private void record_start(String pkgname) {
        String appName = getAppLabel(pkgname);
        long l = System.currentTimeMillis();
        app_first=false;
        app= appDao.getAppByName(appName);

        if(app==null){
            app=new App();
            app.setFirstRunningTime(l);
            app.setPkgName(pkgname);
            app.setAppName(appName);
            app_first=true;
        }

        Log.d("TAG", "got app: "+app.toString()+" first="+app_first);

        oneUse=new OneUse();
        oneUse.setPkgName(pkgname);
        oneUse.setAppName(appName);
        oneUse.setStartTimestamp(l);
        DBUtils.storeBatteryInfo(context,oneUse,true);

    }

    //结束记录
    private void record_finish(String pkgname) {
        if (oneUse.getPkgName().equals(pkgname)) {
            long l = System.currentTimeMillis();
            long spend = l - oneUse.getStartTimestamp();
            //不记录0.5秒内的
            if (spend < 500) return;
            oneUse.setSpendTime(spend);
            app.setLastRuningTime(l);
            app.addTotalRuningTime(spend);//增加统计表的时间
            app.addUseCount();//增加使用次数
            DBUtils.storeBatteryInfo(context, oneUse, false);
            DBUtils.storeNetworkInfo(context, oneUse);
            Log.d("TAG", "got app: " + app.toString() + " first=" + app_first);
            if (app_first)
                appDao.insert(app);
            else
                appDao.updateApp(app);

            appDao.insert(oneUse);

            SimpleApp app = new SimpleApp();
            app.setAppName(oneUse.getAppName());
            app.setPkgName(oneUse.getPkgName());
            if (last_simple_app != null)
                pedictor.updateMatrix(last_simple_app);
            if (!app.equals(last_simple_app)) { //只有打开不同应用时，才进行预测
                OnePred onePred = pedictor.verifyPred(app);
                if (onePred != null)
                    appDao.insert(onePred);
                Log.d("cedar", "MyRecorder2: " + onePred);
            }
            last_simple_app = app;
        }
        else {
            record_start(pkgname);
        }
    }

    public void record(AccessibilityEvent event) {
        String pkgname = event.getPackageName() == null ? "" : event.getPackageName().toString();
        String appLabel = getAppLabel(pkgname);
        if(record_events){
            Event mEvent=new Event();
            mEvent.setAppName(appLabel);
            mEvent.setPkgName(pkgname);
            mEvent.setEventType(event.getEventType());
            mEvent.setTimeStamp(System.currentTimeMillis());
            appDao.insert(mEvent);
        }

        switch (event.getEventType()) {
            //窗口变化时触发
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED: //后台弹出也会触发，用skip
                if (filter_skip && skip_names.contains(appLabel)) return;
                if (filter_exclude && exclude_names.contains(appLabel)) {
                    if (lastPkgname != "")
                        record_finish(lastPkgname);
                    lastPkgname = "";
                    return;
                }

                if (appLabel.equals("") || appLabel == null) return;
                if (pkgname.equals(lastPkgname)) {
                } else {
                    if (!lastPkgname.equals(""))
                        record_finish(lastPkgname);
                    record_start(pkgname);
                    lastPkgname = pkgname;
                }

                break;

            //点击时才触发,滑动没用，用exclude
            case TYPE_VIEW_CLICKED:
                if (filter_exclude && exclude_names.contains(appLabel)) {
                    if (lastPkgname != "")
                        record_finish(lastPkgname);
                    lastPkgname = "";
                    return;
                }
                if (appLabel.equals("") || appLabel == null) return;

                if (pkgname.equals(lastPkgname)) {

                } else {
                    if (!lastPkgname.equals(""))
                        record_finish(lastPkgname);
                    record_start(pkgname);
                    lastPkgname = pkgname;
                }
                break;


        }

    }
    public void close(){
        appDB.close();
    }
}
