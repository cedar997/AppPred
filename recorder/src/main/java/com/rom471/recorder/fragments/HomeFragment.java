package com.rom471.recorder.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.rom471.db.RecordDBHelper;
import com.rom471.db.RecordListViewAdapter;
import com.rom471.present.AppBean;
import com.rom471.present.AppListViewAdapter;
import com.rom471.recorder.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    ListView list_view;
    Context context;
    RecordDBHelper db;
    AppListViewAdapter mAdapter;
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
        db=new RecordDBHelper(getContext(),"app.db");
        List<AppBean> lastAppBean = db.getLastAppBean(10);
//        List<AppBean> lastAppBean=new ArrayList<>();
//        lastAppBean.add(new AppBean("app1",null));
//        lastAppBean.add(new AppBean("app2",null));
        mAdapter=new AppListViewAdapter(context,lastAppBean);

        list_view.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        List<AppBean> lastAppBean = db.getLastAppBean(10);
        Log.d("cedar", "onResume: "+lastAppBean.get(0).getIcon());
        mAdapter=new AppListViewAdapter(context,lastAppBean);

        list_view.setAdapter(mAdapter);
    }
}
