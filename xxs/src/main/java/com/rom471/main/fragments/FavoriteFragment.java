package com.rom471.main.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;


import com.rom471.lab1.R;
import com.rom471.main.HomeActivity;
import com.rom471.room.AppBean;
import com.rom471.room.AppDAO;
import com.rom471.room.AppDataBase;
import com.rom471.userdb.FavoriteAppListViewAdapter;

import java.util.List;

public class FavoriteFragment extends Fragment implements AdapterView.OnItemClickListener {

    ListView favorite_listview;
    AppDAO dao;
    LiveData<List<AppBean>> allFavoriteApps;
    List<AppBean> appBeans;
    Button add_btn;
    PackageManager pm;
    TextView tile_tv;
    View selectView;
    FavoriteAppListViewAdapter mAdapter;
    public FavoriteFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.layout_like_fragment,container,false);
    }
    private AppDAO getDao(){
        String db_name = ((HomeActivity) getActivity()).getName() +".db";
        AppDataBase db = Room.databaseBuilder(getContext(), AppDataBase.class, db_name).allowMainThreadQueries().build();
        return db.getAppDAO();
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tile_tv=getActivity().findViewById(R.id.like_tile);
        String name = ((HomeActivity) getActivity()).getName();
        tile_tv.setText(name+"的收藏列表");
        favorite_listview =getActivity().findViewById(R.id.users_listview);
        add_btn=getActivity().findViewById(R.id.add);

        dao =getDao();
        allFavoriteApps=dao.getAllApps();
        favorite_listview.setOnItemClickListener(FavoriteFragment.this);
        allFavoriteApps.observe((LifecycleOwner) getActivity(), new Observer<List<AppBean>>() {
            @Override
            public void onChanged(List<AppBean> apps) {
                mAdapter=new FavoriteAppListViewAdapter(getContext(),R.layout.favorite_app_item, apps);
                favorite_listview.setAdapter(mAdapter);
            }
        });
        favorite_listview.setAdapter(mAdapter);
        registerForContextMenu(favorite_listview);

    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 0, Menu.NONE, "打开应用");
        menu.add(0, 1, Menu.NONE, "移除收藏");

    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getMenuInfo() instanceof AdapterView.AdapterContextMenuInfo){
            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            AppBean app=mAdapter.get( menuInfo.position);
            switch (item.getItemId()) {
                case 0:
                    Toast.makeText(getContext(), "打开中", Toast.LENGTH_SHORT).show();
                    try {
                        Intent intent =getContext().getPackageManager().getLaunchIntentForPackage(app.getPkgname());
                        getContext().startActivity(intent);
                    } catch (Exception e) {

                        e.printStackTrace();
                    }


                    break;

                case 1:
                    Toast.makeText(getContext(), "删除操作", Toast.LENGTH_SHORT).show();
                    if(selectView!=null){
                        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.item_remove_anim);


                        selectView.startAnimation(animation);
                        selectView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                selectView.setVisibility(View.GONE);
                            }
                        }, 1500);
                    }

                    try {
                        dao.deleteApp(app);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }

        return super.onContextItemSelected(item);
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        view.showContextMenu(view.getX(),view.getY());

        selectView=view;

        view.showContextMenu();
    }
}
