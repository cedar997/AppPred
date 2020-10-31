package com.rom471.main.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.rom471.adapters.FavoriteListAdapter;
import com.rom471.appsdb.AppBean;
import com.rom471.lab1.R;
import com.rom471.main.HomeActivity;

public class FavoriteFragment extends Fragment {
    RecyclerView favoriteListview; //列表视图
    TextView tile_tv;
    FavoriteListAdapter mAdapter;
    public FavoriteFragment(){
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_like_fragment, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //绑定资源
        tile_tv=getActivity().findViewById(R.id.like_tile);
        String userName = ((HomeActivity) getActivity()).getName();
        String db_name=userName+".db";
        favoriteListview =getActivity().findViewById(R.id.users_listview);
        mAdapter=new FavoriteListAdapter(getContext(),db_name);

        favoriteListview.setLayoutManager(new GridLayoutManager(getContext(),2));
        favoriteListview.setAdapter(mAdapter);

        //给recyclerview设置动画
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setRemoveDuration(300);
        favoriteListview.setItemAnimator(defaultItemAnimator);
        //设置标题标签
        tile_tv.setText(userName+"的收藏列表");

    }
    public boolean hasAppName(String appname){
        return mAdapter.hasAppName(appname);
    }
    public void insertApp(AppBean app){
        mAdapter.insertApp(app);
    }
}
