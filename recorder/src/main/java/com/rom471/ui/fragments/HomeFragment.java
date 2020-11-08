package com.rom471.ui.fragments;

import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.rom471.adapter.AppsAdapter;
import com.rom471.recorder.R;
import com.rom471.db.AppBean;
import com.rom471.db.RecordDAO;
import com.rom471.db.RecordDataBase;

import java.util.List;

public class HomeFragment extends Fragment {
    RecyclerView list_view;
    RecyclerView total_list_view;
    List<AppBean> lastApps;
    List<AppBean> totalApps;
    Context context;
    RecordDataBase recordDataBase;
    RecordDAO dao;

    //RecordDBHelper db;
    AppsAdapter totalAdapter;
    AppsAdapter lastAdapter;
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
        //db=new RecordDBHelper(getContext(),"app.db");
        recordDataBase= Room.databaseBuilder(context, RecordDataBase.class, "records.db").allowMainThreadQueries().build();
        dao=recordDataBase.getRecordDAO();



    }

    @Override
    public void onResume() {
        super.onResume();

        lastApps = dao.getLastApp(APP_LIST_SIZE);
        setAppIcon(lastApps);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(context);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        list_view.setLayoutManager(layoutManager1);
        lastAdapter=new AppsAdapter(lastApps);
        list_view.setAdapter(lastAdapter);

        totalApps = dao.getAppTotalTime(10);
        setAppIcon(totalApps);
        totalAdapter =new AppsAdapter(totalApps);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        total_list_view.setLayoutManager(layoutManager);
        total_list_view.setAdapter(totalAdapter);



    }
    private void setAppIcon(List<AppBean> apps){
        PackageManager pm =context.getPackageManager();
        ApplicationInfo appInfo;
        Drawable appIcon;
        for (AppBean app:apps
             ) {
            try {
                appInfo = pm.getApplicationInfo(app.getPkgname(), PackageManager.GET_META_DATA);
                appIcon = pm.getApplicationIcon(appInfo);
                app.setIcon(appIcon);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
