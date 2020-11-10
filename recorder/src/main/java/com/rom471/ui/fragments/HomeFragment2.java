package com.rom471.ui.fragments;

import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rom471.adapter.AppsLastUseAdapter;
import com.rom471.adapter.AppsTotalCountAdapter;
import com.rom471.adapter.AppsTotalTimeAdapter;
import com.rom471.db2.App;
import com.rom471.db2.AppRecordsRepository;
import com.rom471.db2.OneUse;
import com.rom471.recorder.R;

import java.util.List;

public class HomeFragment2 extends Fragment {
    RecyclerView last_list_view;
    RecyclerView total_list_view;
    RecyclerView count_list_view;
    List<App>  appLists;
    LiveData<List<OneUse>> useLists;
    Context context;
    AppRecordsRepository appRecordsRepository;

    //RecordDBHelper db;
    AppsLastUseAdapter lastUseAdapter;
    AppsTotalTimeAdapter totalTimeAdapter;
    AppsTotalCountAdapter totalCountAdapter;
    public static final int APP_LIST_SIZE=100;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment_home,container,false);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context=getContext();

        last_list_view =getActivity().findViewById(R.id.app_last_use_list);
        total_list_view =getActivity().findViewById(R.id.app_total_use_list);
        count_list_view =getActivity().findViewById(R.id.app_total_count_list);


        appRecordsRepository=new AppRecordsRepository(getActivity().getApplication());
        //
        totalTimeAdapter =new AppsTotalTimeAdapter(this,appRecordsRepository);
        lastUseAdapter =new AppsLastUseAdapter(this,appRecordsRepository);

        totalCountAdapter=new AppsTotalCountAdapter(this,appRecordsRepository);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(context);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        last_list_view.setLayoutManager(layoutManager1);
//
        last_list_view.setAdapter(lastUseAdapter);


        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        total_list_view.setLayoutManager(layoutManager);
        total_list_view.setAdapter(totalTimeAdapter);

        LinearLayoutManager layoutManager3 = new LinearLayoutManager(context);
        layoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        count_list_view.setLayoutManager(layoutManager3);
        count_list_view.setAdapter(totalCountAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();






        //useLists = dao.getAppTotalTime(10);
        //setAppIcon(useLists);




    }
    private void setAppIcon(List<App> apps){
        PackageManager pm =context.getPackageManager();
        ApplicationInfo appInfo;
        Drawable appIcon;
        for (App app:apps
        ) {
            try {
                appInfo = pm.getApplicationInfo(app.getPkgName(), PackageManager.GET_META_DATA);
                appIcon = pm.getApplicationIcon(appInfo);
                app.setIcon(appIcon);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}