package com.rom471.recorder;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.rom471.db.RecordDBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

public class RecordService extends Service {
    public static final String TAG ="CEDAR";
    private  int intCounter;
    private Handler myhandler =  new Handler();
    private RecordDBHelper db;
    public RecordService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        db=new RecordDBHelper(getApplicationContext(),"app.db");
        //db.createTable();
    }
    private Runnable myTasks= new Runnable() {
        /**
         * 进程运行
         */
        @Override
        public  void run() {
            //  TODO Auto-generated method stub
            // 递增counter整数，作为后台服务运行时间识别
            intCounter++;
            // 以Log 对象在LogCat 里输出Log信息，监看服务运行情况

            String app_name=getTopActivity(getApplicationContext());
            BatteryManager manager = (BatteryManager) getSystemService(Context.BATTERY_SERVICE);
            int intProperty = manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);
            Toast.makeText(getApplicationContext(),"电量："+intProperty,Toast.LENGTH_SHORT).show();
            db.insertRecord(app_name);

            myhandler.postDelayed(myTasks, 2000);
        }
    };
    @Override
    public  void onStart(Intent intent, int startId){
        myhandler.postDelayed(myTasks, 1000);
        super.onStart(intent, startId);
        Log.i("Start Service", "onStart");
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static String getTopActivity(Context context) {
        try {
            List<UsageEvents.Event> allEvents = new ArrayList<>();
            HashMap<String, Integer> appUsageMap = new HashMap<>();
            UsageEvents.Event lastEvent = allEvents.get(allEvents.size() - 1);
            if(lastEvent.getEventType() == UsageEvents.Event.ACTIVITY_RESUMED) {
                int diff = (int)System.currentTimeMillis() - (int)lastEvent.getTimeStamp();
                diff /= 1000;
                Integer prev = appUsageMap.get(lastEvent.getPackageName());
                if(prev == null) prev = 0;
                appUsageMap.put(lastEvent.getPackageName(), prev + diff);
            }

        } catch (Exception e) {
            Log.d("GsonUtils", "Exception=" + e.toString());
        }
        return "";
    }
}