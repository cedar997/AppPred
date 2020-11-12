package com.rom471.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
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


import com.rom471.db2.AppRecordsRepository;
import com.rom471.recorder.R;
import com.rom471.services.RecordService;

import com.rom471.utils.DBUtils;

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
    AppRecordsRepository appRecordsRepository;
    Context context;
    SharedPreferences properties;


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
        //db=new RecordDBHelper(getContext(),"app.db");
        appRecordsRepository=new AppRecordsRepository(getActivity().getApplication());
        accessibility_btn=getActivity().findViewById(R.id.open_accessibility_btn);
        normal_service_btn=getActivity().findViewById(R.id.start_service_btn);

        accessibility_btn.setOnClickListener(this);
        clearRecord_btn=getActivity().findViewById(R.id.clear_records_btn);
        ouput_db_btn=getActivity().findViewById(R.id.output_db_btn);
        ouput_db_btn.setOnClickListener(this);
        clearRecord_btn.setOnClickListener(this);
        normal_service_btn.setOnClickListener(this);
//        Log.d("cedar", "onActivityCreated: "+ MyProperties.getProperties(context).getString("dbname",""));

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
                Intent recoder_service=new Intent(context, RecordService.class);
                context.startService(recoder_service);
                toast(context,"记录服务已经启动");
                break;
            case R.id.output_db_btn:
                if(verifyStoragePermissions(getActivity())) {
                    export_db();
                }
                else {

                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean accessibilitySettingsOn = isAccessibilitySettingsOn(context);
        if(accessibilitySettingsOn){
            accessibility_btn.setText("辅助功能已经打开");
        }
        else {
            accessibility_btn.setText("点击打开辅助功能");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void export_db(){

        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;

        int db_index=0;//数据库序号


        String currentDBPath = "/data/"+ "com.rom471.recorder2" +"/databases/"+"apps.db";
        String backupPath=sd+"/rom471/";
        File backupPathFile = new File(backupPath);
        if (!backupPathFile.exists()) { //检查目录是否存在
            try {
                //按照指定的路径创建文件夹
                backupPathFile.mkdirs();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
       // String dbname =(String) MyProperties.get(context,"dbname","app");//共享配置
        String dbname= DBUtils.getCurrentDBString();
        String backupDBName=dbname+".db";
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
                        appRecordsRepository.deleteAll();
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
    private static boolean isAccessibilitySettingsOn(Context context) {
        String service="com.rom471.recorder/com.rom471.recorder.AccessibilityMonitorService";
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
        String settingValue = Settings.Secure.getString(
                context.getApplicationContext().getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        Log.d("cedar", "isAccessibilitySettingsOn: "+settingValue);
        if (settingValue != null) {
            mStringColonSplitter.setString(settingValue); //各个服务由分号分割
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
    public static boolean verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
