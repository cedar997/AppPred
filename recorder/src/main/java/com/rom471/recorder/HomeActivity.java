package com.rom471.recorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.rom471.db.RecordListViewAdapter;
import com.rom471.db.Record;
import com.rom471.db.RecordDBHelper;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    ListView list_view;
    Button start_service_btn;
    Button stop_service_btn;
    Button clear_records_btn;
    Button accessibility_btn;
    RecordDBHelper db;
    Intent recoder_service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        list_view=findViewById(R.id.app_list);
        db=new RecordDBHelper(this,"app.db");

        updateRecordListView();
        start_service_btn=findViewById(R.id.start_service_btn);
        stop_service_btn=findViewById(R.id.stop_service_btn);
        clear_records_btn=findViewById(R.id.clear_records_btn);
        accessibility_btn=findViewById(R.id.open_accessibility_btn);

        start_service_btn.setOnClickListener(this);
        stop_service_btn.setOnClickListener(this);
        clear_records_btn.setOnClickListener(this);
        accessibility_btn.setOnClickListener(this);
    }
    private void updateRecordListView(){
        List<Record> records = db.queryAll();
        RecordListViewAdapter mAdapter=new RecordListViewAdapter(this,records);
        list_view.setAdapter(mAdapter);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_service_btn:
                recoder_service=new Intent(this,RecordService.class);
                startService(recoder_service);
                toast(this,"记录服务已经启动");
                break;
            case R.id.stop_service_btn:

                //stopService(recoder_service);
                toast(this,"页面已经刷新");
                updateRecordListView();
                break;
            case R.id.clear_records_btn:
                db.clearRecords();
                updateRecordListView();
                break;
            case R.id.open_accessibility_btn:
                getAccessibilityPermission(this);
                break;
        }
    }
    public  void toast(Context context, String text){
        Toast toast=Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    public void getAccessibilityPermission(Context context){
        final String mAction= Settings.ACTION_ACCESSIBILITY_SETTINGS;//辅助功能
        Intent intent=new Intent(mAction);
        context.startActivity(intent);
    }
}