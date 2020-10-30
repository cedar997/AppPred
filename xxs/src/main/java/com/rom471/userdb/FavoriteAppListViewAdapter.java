package com.rom471.userdb;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rom471.lab1.R;
import com.rom471.room.AppBean;

import java.util.List;

public class FavoriteAppListViewAdapter extends ArrayAdapter<AppBean> {
    Context context;
    int resource;
    List<AppBean> favorite_apps;
    PackageManager pm;
    SectionIndexer sectionIndexer;
    public FavoriteAppListViewAdapter(@NonNull Context context, int resource, List<AppBean> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.favorite_apps =objects;
        pm=context.getPackageManager();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=null;
        LinearLayout appsLayout;
        TextView name_tv;
        ImageView icon_img;

        if(convertView!=null){
            view=convertView;
        }
        else {
            view= LayoutInflater.from(context).inflate(R.layout.favorite_app_item,null);
        }
        appsLayout=view.findViewById(R.id.user_list_layout);
        name_tv=view.findViewById(R.id.item_name);
        icon_img=view.findViewById(R.id.item_avatar);

        AppBean appBean = favorite_apps.get(position);
        name_tv.setText(appBean.getAppname());
        icon_img.setBackground(getIcon(appBean.getPkgname()));
        return view;
    }
    private Drawable getIcon(String pkgname) {
        PackageManager pm =context.getPackageManager();
        ApplicationInfo appInfo;
        Drawable appIcon;
        try {
            appInfo = pm.getApplicationInfo(pkgname, PackageManager.GET_META_DATA);

            appIcon = pm.getApplicationIcon(appInfo);
            return appIcon;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AppBean get(int position) {
        return favorite_apps.get(position);
    }
}
