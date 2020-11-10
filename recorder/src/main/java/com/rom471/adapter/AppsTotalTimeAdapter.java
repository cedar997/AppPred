package com.rom471.adapter;

import android.content.Context;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.rom471.db2.App;
import com.rom471.db2.AppRecordsRepository;
import com.rom471.utils.DBUtils;

import java.util.List;

public class AppsTotalTimeAdapter extends AppsAdapter1 {
    public static final int APP_LIST_SIZE=100;
    @Override
    public void setAppInfo(TextView appInfo, App app) {

        appInfo.setText( DBUtils.getTimeSpendString(app.getTotalRuningTime()));
    }

    public AppsTotalTimeAdapter(Fragment context, AppRecordsRepository appRecordsRepository){
        appRecordsRepository.getMostUsedApps(APP_LIST_SIZE).observe( context, new Observer<List<App>>() {
            @Override
            public void onChanged(List<App> apps) {
                DBUtils.setAppsIcon(context.getContext(),apps);
                setmAppsList(apps);
                notifyDataSetChanged();
            }
        });
    }

}
