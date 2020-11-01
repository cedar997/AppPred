package com.rom471.main.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import com.rom471.adapters.AllListAdapter;
import com.rom471.lab1.R;
import com.rom471.appsdb.AppBean;
import java.util.List;

public class AllAppsFragment extends Fragment  implements View.OnClickListener, TextWatcher {
    AllListAdapter appAdapter;
    ListView app_list_view;
    ImageView search_btn;
    EditText search_box;
    PackageManager pm;
    FavoriteFragment favoriteFragment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_all_fragment,container,false);

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fragmentManager=getFragmentManager();
        favoriteFragment=(FavoriteFragment) fragmentManager.findFragmentByTag("favorite");
        search_btn=getActivity().findViewById(R.id.search_btn);
        search_box=getActivity().findViewById(R.id.search_box);
        search_btn.setOnClickListener(this);
        search_box.addTextChangedListener(this);
        app_list_view=getActivity().findViewById(R.id.app_list);
         appAdapter =new AllListAdapter(getContext());
        pm=getContext().getPackageManager();
        getApplist();
        app_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppBean app=appAdapter.get(position);
                if(favoriteFragment!=null && favoriteFragment.hasAppName(app.getAppname())){
                    Toast.makeText(getContext(),"该应用已经被收藏",Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    favoriteFragment.insertApp(app);
                    Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.item_add_anim);
                    view.startAnimation(animation);
                    appAdapter.notifyDataSetChanged();
                }catch (Exception e){
                    e.printStackTrace();

                }
            }
        });
    }
    private void getApplist(){

        List<PackageInfo> packages=pm.getInstalledPackages(0);
        appAdapter =new AllListAdapter(getContext());
        for(PackageInfo packageInfo:packages){
            if((packageInfo.applicationInfo.flags& ApplicationInfo.FLAG_SYSTEM)==0) {//非系统应用
                ApplicationInfo info = packageInfo.applicationInfo;
                AppBean app=new AppBean();
                String name = info.loadLabel(pm).toString();
                app.setAppname(name);
                app.setPkgname(info.packageName);
                app.setIcon(info.loadIcon(pm));
                app.setLiked(favoriteFragment.hasAppName(name));
                appAdapter.add(app);
            }
            else{ //系统应用，暂时不添加
//                ApplicationInfo info = new ApplicationInfo();
//                info.name = packageInfo.applicationInfo.loadLabel(pm).toString();
//                AppBean app=new AppBean();
//                String name = info.loadLabel(pm).toString();
//                app.setAppname(name);
//                app.setPkgname(info.packageName);
//                app.setIcon(info.loadIcon(pm));
//                app.setLiked(favoriteFragment.hasAppName(name));
//                appAdapter.add(app);
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
