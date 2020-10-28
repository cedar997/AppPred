package com.rom471.recorder.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;


import com.rom471.db.RecordDBHelper;
import com.rom471.recorder.R;
import com.rom471.recorder.RecordService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;


public class SettingsFragment extends Fragment implements View.OnClickListener{

    Button accessibility_btn;
    Button clearRecord_btn;
    Button normal_service_btn;
    Button ouput_db_btn;
    RecordDBHelper db;
    Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment_settings,container,false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context=getContext();
        db=new RecordDBHelper(getContext(),"app.db");
        accessibility_btn=getActivity().findViewById(R.id.open_accessibility_btn);
        normal_service_btn=getActivity().findViewById(R.id.start_service_btn);

        accessibility_btn.setOnClickListener(this);
        clearRecord_btn=getActivity().findViewById(R.id.clear_records_btn);
        ouput_db_btn=getActivity().findViewById(R.id.output_db_btn);
        ouput_db_btn.setOnClickListener(this);
        clearRecord_btn.setOnClickListener(this);
        normal_service_btn.setOnClickListener(this);
        boolean accessibilitySettingsOn = isAccessibilitySettingsOn(context, "com.rom471.recorder.AccessibilityMonitorService");
        if(accessibilitySettingsOn){
            accessibility_btn.setBackgroundColor(Color.GREEN);
        }
        else {
            accessibility_btn.setBackgroundColor(Color.GRAY);
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.clear_records_btn:

                confirmClearRecordsDialog();

                break;
            case R.id.open_accessibility_btn:
                getAccessibilityPermission(getContext());
                break;
            case R.id.start_service_btn:
                Intent recoder_service=new Intent(context,RecordService.class);
                context.startService(recoder_service);
                toast(context,"记录服务已经启动");
                break;
            case R.id.output_db_btn:
                export_db();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void export_db(){
        verifyStoragePermissions(getActivity());
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        String DB_NAME="app.db";
        String currentDBPath = "/data/"+ "com.rom471.recorder" +"/databases/"+DB_NAME;
        Calendar calendar = Calendar.getInstance();

        String backupDBName =""+(calendar.get(Calendar.MONTH)+1)+"月"+
                calendar.get(Calendar.DAY_OF_MONTH)+"日"+
                calendar.get(Calendar.HOUR_OF_DAY)+"时"+
                calendar.get(Calendar.MINUTE)+"分"+
                ".db" ;
        String backupPath=sd+"/rom471/";
        File backupPathFile = new File(backupPath);
        if (!backupPathFile.exists()) {
            try {
                //按照指定的路径创建文件夹
                backupPathFile.mkdirs();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(backupPath, backupDBName);

        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(context, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    //确认清除数据的弹出对话框
    public void confirmClearRecordsDialog(){
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setIcon(R.drawable.ic_launcher_background);
        normalDialog.setTitle("警告！");
        normalDialog.setMessage("确认要删除已有的数据吗？");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.clearRecords();
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        normalDialog.show();
    }
    //跳转到打开辅助功能界面
    public void getAccessibilityPermission(Context context){
        final String mAction= Settings.ACTION_ACCESSIBILITY_SETTINGS;//辅助功能
        Intent intent=new Intent(mAction);
        context.startActivity(intent);
    }
    private static boolean isAccessibilitySettingsOn(Context context, String service) {

        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
        String settingValue = Settings.Secure.getString(
                context.getApplicationContext().getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        Log.d("cedar", "isAccessibilitySettingsOn: "+settingValue);
        if (settingValue != null) {
            mStringColonSplitter.setString(settingValue);
            while (mStringColonSplitter.hasNext()) {
                String accessibilityService = mStringColonSplitter.next();
                if (accessibilityService.equalsIgnoreCase(service)) {
                    return true;
                }
            }
        }
        return false;
    }

    public  void toast(Context context, String text){
        Toast toast=Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    //先定义
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    //然后通过一个函数来申请
    public static void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
