package com.rom471.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;


import com.rom471.lab1.R;
import com.rom471.appsdb.AppBean;
import com.rom471.appsdb.AppDAO;
import com.rom471.appsdb.AppDataBase;

import java.util.List;

public class FavoriteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AppBean> mAppsList;
    private final int SHOW_MENU = 1;
    private final int HIDE_MENU = 2;
    AppDAO dao;  //用户收藏数据库操作类
    Context context;
    PackageManager pm;
    public FavoriteListAdapter(Context context, String db_name) {
        this.context=context;
        this.mAppsList = mAppsList;
        dao=getDao(db_name);
        initApps();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType==SHOW_MENU){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_app_item_menu,parent,false);
            return new MenuViewHolder((view));
        }
        else {
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_app_item,parent,false);
            ViewHolder holder=new ViewHolder(view);
            return holder;
        }



    }
    public void showMenu(int position) {
        for(int i=0; i<mAppsList.size(); i++){
            mAppsList.get(i).setShowMenu(false);
        }
        mAppsList.get(position).setShowMenu(true);
        notifyDataSetChanged();
    }


    public boolean isMenuShown() {
        for(int i=0; i<mAppsList.size(); i++){
            if(mAppsList.get(i).isShowMenu()){
                return true;
            }
        }
        return false;
    }

    public void closeMenu() {
        for(int i=0; i<mAppsList.size(); i++){
            mAppsList.get(i).setShowMenu(false);
        }
        notifyDataSetChanged();
    }
    //渲染数据到View中
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ViewHolder){
            ViewHolder mHolder=(ViewHolder) holder;
            AppBean app=mAppsList.get(position);
            mHolder.appIcon.setBackground(app.getIcon());
            mHolder.appName.setText(app.getAppname());
            mHolder.list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMenu(position);

                }
            });
        }
        if(holder instanceof MenuViewHolder){
            MenuViewHolder mHolder=(MenuViewHolder) holder;
            AppBean app=mAppsList.get(position);
//            mHolder.appIcon.setBackground(app.getIcon());
            mHolder.appName.setText(app.getAppname());
            mHolder.list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeMenu();
                }
            });
            mHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemRemoved(position);
                    dao.deleteApp(mAppsList.get(position));
                    mAppsList.remove(position);
                   // notifyDataSetChanged();

//

                }
            });
            mHolder.open.setOnClickListener(new MyOnclickListenr(context,mAppsList.get(position).getPkgname()));
        }

    }
    private class MyOnclickListenr implements View.OnClickListener{
        Context context;
        String pkgname;
        public MyOnclickListenr(Context context,String pkgname){
            this.context=context;
            this.pkgname=pkgname;
        }
        @Override
        public void onClick(View v) {
            try {
                Intent intent =context.getPackageManager().getLaunchIntentForPackage(pkgname);
                context.startActivity(intent);
                toast("启动成功");
            }catch (Exception e){
                toast("打开失败");
            }

        }
    }
    @Override
    public int getItemViewType(int position) {
        if(mAppsList.get(position).isShowMenu()){
            return SHOW_MENU;
        }else{
            return HIDE_MENU;
        }
    }
    @Override
    public int getItemCount() {
        return mAppsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView appIcon;
        TextView appName;
        LinearLayout list;

        public ViewHolder(View view) {
            super(view);
            appIcon = (ImageView) view.findViewById(R.id.item_icon);
            appName = (TextView) view.findViewById(R.id.item_name);
            list=view.findViewById(R.id.item_list_layout);

        }

    }
    public class MenuViewHolder extends RecyclerView.ViewHolder{
//        ImageView appIcon;
        TextView appName;
        LinearLayout list;
        Button delete;
        Button open;

        public MenuViewHolder(View view){
            super(view);
            list=view.findViewById(R.id.item_list_layout);
//            appIcon = (ImageView) view.findViewById(R.id.item_icon);
            appName = (TextView) view.findViewById(R.id.item_name);
            delete=(Button) view.findViewById(R.id.recycle_menu_delete);
            open=(Button) view.findViewById(R.id.recycle_menu_open);

        }
    }
    private AppDAO getDao(String db_name){
        AppDataBase db = Room.databaseBuilder(context, AppDataBase.class, db_name).allowMainThreadQueries().build();
        return db.getAppDAO();
    }
    private void initApps(){
        pm=context.getPackageManager();
        mAppsList=dao.getAllApps();
        for (AppBean app:mAppsList
        ) {
            try {
                ApplicationInfo appInfo = pm.getApplicationInfo(app.getPkgname(), PackageManager.GET_META_DATA);

                Drawable appIcon = pm.getApplicationIcon(appInfo);
                app.setIcon(appIcon);
                app.setShowMenu(false);

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean hasAppName(String appname){

        for (AppBean app:
             mAppsList) {
            if(app.getAppname().equals(appname)){

                return true;
            }
        }
        return false;
    }
    public void insertApp(AppBean app){
        app.setLiked(true);
        mAppsList.add(app);
        dao.insertApp(app);

    }
    public  void toast( String text){
        Toast toast=Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
