package com.rom471.recorder.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


import com.rom471.db.Record;
import com.rom471.db.RecordDBHelper;
import com.rom471.recorder.R;
import com.rom471.recorder.RecordService;

import java.util.List;


public class SettingsFragment extends Fragment implements View.OnClickListener{

    Button accessibility_btn;
    Button clearRecord_btn;
    Button normal_service_btn;
    RecordDBHelper db;
    Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_settings_fragment,container,false);
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
        clearRecord_btn.setOnClickListener(this);
        normal_service_btn.setOnClickListener(this);


    }
    @RequiresApi(api = Build.VERSION_CODES.M)
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
        }
    }
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
    public void getAccessibilityPermission(Context context){
        final String mAction= Settings.ACTION_ACCESSIBILITY_SETTINGS;//辅助功能
        Intent intent=new Intent(mAction);
        context.startActivity(intent);
    }
    public  void toast(Context context, String text){
        Toast toast=Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
