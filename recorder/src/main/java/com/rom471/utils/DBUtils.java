package com.rom471.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;

import com.rom471.db.AppBean;
import com.rom471.db.Record;

import java.util.List;

import static android.content.Context.BATTERY_SERVICE;

public class DBUtils {
    //给record附加电池信息
    public static void storeBatteryInfo(Context context, Record record){
        BatteryManager manager = (BatteryManager)context.getSystemService(BATTERY_SERVICE);

        record.setBattery(manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY));

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        // Are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
//        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
//                status == BatteryManager.BATTERY_STATUS_FULL;

        // How are we charging?
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        int charging=0;
        if(acCharge)charging=1;
        if(usbCharge) charging=2;
        record.setCharging(charging);

    }
    //给record附加网络信息
    public static void storeNetworkInfo(Context context, Record record){
        int net_state=0;
        ConnectivityManager manager= (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        //判断NetworkInfo对象是否为空
        if (networkInfo != null) {

            if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){//使用数据
                net_state=1;
            }
            else if(networkInfo.getType()==ConnectivityManager.TYPE_WIFI){//使用wifi
                net_state=2;
            }
        }
        record.setNet(net_state);

    }
    //给记录设置应用图标
    public static void setAppIcon(Context context,List<Record> apps){
        PackageManager pm =context.getPackageManager();
        ApplicationInfo appInfo;
        Drawable appIcon;
        for (Record app:apps
        ) {
            try {
                appInfo = pm.getApplicationInfo(app.getPkgname(), PackageManager.GET_META_DATA);
                appIcon = pm.getApplicationIcon(appInfo);
                app.setIcon(appIcon);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
