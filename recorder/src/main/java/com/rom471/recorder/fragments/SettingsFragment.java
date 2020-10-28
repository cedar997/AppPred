package com.rom471.recorder.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.rom471.db.AppListViewAdapter;
import com.rom471.db.Record;
import com.rom471.db.RecordDBHelper;
import com.rom471.recorder.R;
import com.rom471.recorder.RecordService;

import java.util.List;


public class SettingsFragment extends Fragment implements View.OnClickListener{

    Button accessibility_btn;
    RecordDBHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_settings_fragment,container,false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db=new RecordDBHelper(getContext(),"app.db");


    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.clear_records_btn:
                db.clearRecords();

                break;
            case R.id.open_accessibility_btn:
                getAccessibilityPermission(getContext());
                break;
        }
    }
    public void getAccessibilityPermission(Context context){
        final String mAction= Settings.ACTION_ACCESSIBILITY_SETTINGS;//辅助功能
        Intent intent=new Intent(mAction);
        context.startActivity(intent);
    }
}
