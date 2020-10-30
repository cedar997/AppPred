package com.rom471.main.fragments;

import android.app.Fragment;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;


import com.rom471.appdb.AppListViewAdapter;
import com.rom471.lab1.R;
import com.rom471.main.HomeActivity;
import com.rom471.room.AppBean;
import com.rom471.room.AppDAO;
import com.rom471.room.AppDataBase;

import java.util.List;

public class AllAppsFragment extends Fragment  implements View.OnClickListener, TextWatcher {
    AppListViewAdapter appAdapter;
    ListView app_list_view;
    ImageView search_btn;
    EditText search_box;
    AppDAO dao;
    PackageManager pm;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.layout_all_fragment,container,false);

    }
    private AppDAO getDao(){
        String db_name = ((HomeActivity) getActivity()).getName()+".db";
        AppDataBase db = Room.databaseBuilder(getContext(), AppDataBase.class, db_name).allowMainThreadQueries().build();
        return db.getAppDAO();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        search_btn=getActivity().findViewById(R.id.search_btn);
        search_box=getActivity().findViewById(R.id.search_box);
        search_btn.setOnClickListener(this);
        search_box.addTextChangedListener(this);

        app_list_view=getActivity().findViewById(R.id.app_list);
         appAdapter =new AppListViewAdapter(getContext());
        pm=getContext().getPackageManager();
        getApplist();
        app_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setBackgroundColor(Color.rgb(240,240,240));
                AppBean app=appAdapter.get(position);

                try {
                    dao = getDao();
                    dao.insertApp(app);
                    Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.item_add_anim);
                    view.startAnimation(animation);


                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getContext(),"该应用已经被收藏",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private void getApplist(){

        List<PackageInfo> packages=pm.getInstalledPackages(0);
        appAdapter =new AppListViewAdapter(getContext());
        for(PackageInfo packageInfo:packages){
            if((packageInfo.applicationInfo.flags& ApplicationInfo.FLAG_SYSTEM)==0) {//非系统应用
                ApplicationInfo info = packageInfo.applicationInfo;
                AppBean app=new AppBean();
                String name = info.loadLabel(pm).toString();
                app.setAppname(name);
                app.setPkgname(info.packageName);
                app.setIcon(info.loadIcon(pm));
                appAdapter.add(app);
            }
            else{
//                ApplicationInfo info = new ApplicationInfo();
//                info.name = packageInfo.applicationInfo.loadLabel(pm).toString();
//                mList.add(info);
            }
        }

        app_list_view.setAdapter(appAdapter);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_btn:
                search_box.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        appAdapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
