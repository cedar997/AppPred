package com.rom471.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.rom471.db2.App;
import com.rom471.db2.AppRecordsRepository;
import com.rom471.recorder.R;
import com.rom471.utils.DBUtils;

import java.util.List;

public class AppsLastUseAdapter extends AppsAdapter1 {
    public static final int APP_LIST_SIZE=100;
    @Override
    public void setAppInfo(TextView appInfo, App app) {

        appInfo.setText( DBUtils.getSinceTimeString(app.getLastRuningTime())+"前");
    }
    public AppsLastUseAdapter(Fragment context, AppRecordsRepository appRecordsRepository){
        appRecordsRepository.getLastApp(APP_LIST_SIZE).observe( context, new Observer<List<App>>() {
            @Override
            public void onChanged(List<App> apps) {
                DBUtils.setAppsIcon(context.getContext(),apps);
                setmAppsList(apps);
                notifyDataSetChanged();
            }
        });
    }
}
