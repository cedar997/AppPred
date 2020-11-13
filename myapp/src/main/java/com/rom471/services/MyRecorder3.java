package com.rom471.services;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.rom471.db2.App;
import com.rom471.db2.AppDao;
import com.rom471.db2.AppDataBase;
import com.rom471.db2.OneUse;
import com.rom471.db2.SimpleApp;
import com.rom471.utils.AppUtils;
import com.rom471.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyRecorder3 {
    private static final String TAG = "cedar";
    private String lastName = "";
    // RecordDAO dao;

    AppDao appDao;
    App app;
    OneUse oneUse;
    Context context;
    PackageManager pm;

    List<String> exclude_names;
    List<String> stopApps;
    boolean filter_exclude = false;
    boolean filter_skip = false;
    boolean app_first=false;//当前app是否是第一次插入
    boolean record_events=false;
    AppDataBase appDB;

    public MyRecorder3(Context context) {
        this.context=context;
        appDB =AppDataBase.getInstance(context);
        appDao= appDB.getAppDao();
        pm = context.getPackageManager();//初始化包管理器

        stopApps = getSkip_names();
        filter_exclude = true;//过滤开关
        filter_skip = true;//过滤开关
        record_events=true;


    }

    public List<String> getSkip_names() {
        List<String> apps = new ArrayList<>();
        apps.add("系统 UI");
        apps.add("应用预测");
        apps.add("系统桌面");
        apps.add("设置");
        apps.add("权限管理服务");
        apps.add(("万象息屏"));
        return apps;
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
    private void record_start(String appName) {
        String pkgname = AppUtils.getPkgName(appName);
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
        oneUse=new OneUse();
        oneUse.setPkgName(pkgname);
        oneUse.setAppName(appName);
        oneUse.setStartTimestamp(l);
        DBUtils.storeBatteryInfo(context,oneUse,true);

    }

    //结束记录
    private void record_finish(String lastAppName) {
        if(oneUse==null)
            return;
        if( !oneUse.getAppName().equals(lastAppName)){
            return;
        }
        long l = System.currentTimeMillis();
        long spend = l - oneUse.getStartTimestamp();
        //不记录0.3秒内的
        if (spend < 300) return;
        oneUse.setSpendTime(spend);
        app.setLastRuningTime(l);
        app.addTotalRuningTime(spend);//增加统计表的时间
        app.addUseCount();//增加使用次数
        DBUtils.storeBatteryInfo(context, oneUse, false);
        DBUtils.storeNetworkInfo(context, oneUse);
        if (app_first)
            appDao.insert(app);
        else
            appDao.updateApp(app);

        appDao.insert(oneUse);

        oneUse=null;

    }

    public void record(AccessibilityEvent event) {
        int type=event.getEventType();
        if(type!=32)return;
        String pkgname = event.getPackageName() == null ? "" : event.getPackageName().toString();
        String appName = getAppLabel(pkgname);
        //获取类名
        String name= event.getClassName()==null ?"":event.getClassName().toString();
        if(!name.startsWith("android.")) {
            Log.d(TAG, ""+appName+" "+name);
            //如果回到桌面就停止记录
            if(stopApps.contains(appName)){
                record_finish(lastName);

            }
            //不是桌面程序就开始记录
            else {
                record_start(appName);
                lastName=appName;
            }

        }
    }
    public void close(){
        appDB.close();
    }
}
