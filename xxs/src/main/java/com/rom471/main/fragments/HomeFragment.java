package com.rom471.main.fragments;

import android.app.Fragment;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.rom471.appdb.AppListViewAdapter;
import com.rom471.lab1.R;

import java.util.List;

public class HomeFragment extends Fragment {
    AppListViewAdapter appAdapter;
    ListView app_list_view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_home_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        app_list_view=getActivity().findViewById(R.id.app_list);
         appAdapter =new AppListViewAdapter(getContext());
        getApplist();
    }
    private void getApplist(){
        PackageManager pm=getContext().getPackageManager();
        List<PackageInfo> packages=pm.getInstalledPackages(0);
        appAdapter =new AppListViewAdapter(getContext());
        for(PackageInfo packageInfo:packages){
            if((packageInfo.applicationInfo.flags& ApplicationInfo.FLAG_SYSTEM)==0) {//非系统应用
                ApplicationInfo info = packageInfo.applicationInfo;

                appAdapter.add(info);
            }
            else{
//                ApplicationInfo info = new ApplicationInfo();
//                info.name = packageInfo.applicationInfo.loadLabel(pm).toString();
//                mList.add(info);
            }
        }

        app_list_view.setAdapter(appAdapter);
    }
}
