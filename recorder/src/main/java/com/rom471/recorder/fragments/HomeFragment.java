package com.rom471.recorder.fragments;

import android.app.Fragment;
import android.content.Context;
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

import com.rom471.app.AppsAdapter;
import com.rom471.db.RecordDBHelper;
import com.rom471.app.AppBean;
import com.rom471.recorder.R;

import java.util.List;

public class HomeFragment extends Fragment {
    RecyclerView list_view;
    RecyclerView total_list_view;
    List<AppBean> lastApps;
    List<AppBean> totalApps;
    Context context;
    RecordDBHelper db;
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
        db=new RecordDBHelper(getContext(),"app.db");



    }

    @Override
    public void onResume() {
        super.onResume();

        lastApps = db.getLastAppBean(APP_LIST_SIZE);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(context);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        list_view.setLayoutManager(layoutManager1);
        lastAdapter=new AppsAdapter(lastApps);
        list_view.setAdapter(lastAdapter);

        totalApps = db.getAppTotalTime(10);
        totalAdapter =new AppsAdapter(totalApps);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        total_list_view.setLayoutManager(layoutManager);
        total_list_view.setAdapter(totalAdapter);



    }
}
