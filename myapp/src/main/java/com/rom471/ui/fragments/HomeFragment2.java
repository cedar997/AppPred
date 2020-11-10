package com.rom471.ui.fragments;

import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
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

public class HomeFragment2 extends Fragment implements View.OnClickListener {
    RecyclerView recyclerView;

    List<App>  appLists;
    LiveData<List<OneUse>> useLists;
    Context context;
    AppRecordsRepository appRecordsRepository;
    Button total_time_btn;

    //RecordDBHelper db;
    AppsLastUseAdapter adapter_last;
    AppsTotalTimeAdapter adapter_time_total;
    AppsTotalCountAdapter adapter_count_total;
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

        recyclerView =getActivity().findViewById(R.id.app_list);
        total_time_btn=getActivity().findViewById(R.id.sort_btn);


        total_time_btn.setOnClickListener(this);




        appRecordsRepository=new AppRecordsRepository(getActivity().getApplication());
        //
        adapter_time_total =new AppsTotalTimeAdapter(this,appRecordsRepository);
        adapter_last =new AppsLastUseAdapter(this,appRecordsRepository);

        adapter_count_total =new AppsTotalCountAdapter(this,appRecordsRepository);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(context);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager1);
//
        recyclerView.setAdapter(adapter_last);



    }

    @Override
    public void onResume() {
        super.onResume();

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.sort_btn:
                PopupMenu popup = new PopupMenu(getActivity(), total_time_btn);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.popup_one:
                                recyclerView.setAdapter(adapter_last);
                                break;
                            case R.id.popup_two:
                                recyclerView.setAdapter(adapter_time_total);
                                break;
                            case R.id.popup_three:
                                recyclerView.setAdapter(adapter_count_total);
                                break;
                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
                break;
        }
    }
}
