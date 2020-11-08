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
import androidx.room.Room;

import com.rom471.adapter.AppsAdapter;
import com.rom471.adapter.AppsAdapter2;
import com.rom471.db2.App;
import com.rom471.db2.AppRecordsRepository;
import com.rom471.db2.OneUse;
import com.rom471.recorder.R;
import com.rom471.db.AppBean;
import com.rom471.db.RecordDAO;
import com.rom471.db.RecordDataBase;

import java.util.List;

public class HomeFragment2 extends Fragment {
    RecyclerView list_view;
    RecyclerView total_list_view;
    List<App>  appLists;
    LiveData<List<OneUse>> useLists;
    Context context;
    AppRecordsRepository appRecordsRepository;

    //RecordDBHelper db;
    AppsAdapter2 appAdapter;
    AppsAdapter2 lastAdapter;
    public static final int APP_LIST_SIZE=5;
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

        list_view=getActivity().findViewById(R.id.app_list);
        total_list_view =getActivity().findViewById(R.id.pred_app_list);

        appAdapter=new AppsAdapter2();
        lastAdapter=new AppsAdapter2();
        appRecordsRepository=new AppRecordsRepository(getActivity().getApplication());
        //
        appRecordsRepository.getMostUsedApps(APP_LIST_SIZE).observe(this, new Observer<List<App>>() {
            @Override
            public void onChanged(List<App> apps) {
                Log.d("TAG", "onChanged: 数据库发生了改变");
                appLists=apps;
                setAppIcon(appLists);
                appAdapter.setmAppsList(appLists);
                appAdapter.notifyDataSetChanged();
            }
        });
        appRecordsRepository.getLastApp(APP_LIST_SIZE).observe(this, new Observer<List<App>>() {
            @Override
            public void onChanged(List<App> apps) {
                setAppIcon(apps);
                lastAdapter.setmAppsList(apps);
                lastAdapter.notifyDataSetChanged();
            }
        });
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(context);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        list_view.setLayoutManager(layoutManager1);
//
        list_view.setAdapter(lastAdapter);


        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        total_list_view.setLayoutManager(layoutManager);
        total_list_view.setAdapter(appAdapter);
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
