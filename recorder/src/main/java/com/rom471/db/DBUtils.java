package com.rom471.db;

import android.content.Context;
import android.os.BatteryManager;

import static android.content.Context.BATTERY_SERVICE;

public class DBUtils {
    public static void storeBatteryInfo(Context context, Record record){
        BatteryManager manager = (BatteryManager)context.getSystemService(BATTERY_SERVICE);

        record.setBattery(manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY));
        record.setCharging(manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW));
    }
}
