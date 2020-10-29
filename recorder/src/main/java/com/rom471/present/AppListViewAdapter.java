package com.rom471.present;


import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.rom471.db.Record;
import com.rom471.recorder.R;

import java.util.ArrayList;
import java.util.List;

public class AppListViewAdapter extends BaseAdapter {
    public static final String TAG= AppListViewAdapter.class.getSimpleName();
    private List<AppBean> mApps;

    private LayoutInflater mInflater;
    PackageManager pm;


    public AppListViewAdapter(Context context){
        mApps =new ArrayList<>();

        mInflater=LayoutInflater.from(context);
        pm=context.getPackageManager();
    }
    public AppListViewAdapter(Context context, List<AppBean> mList){
        this.mApps =mList;
        mInflater=LayoutInflater.from(context);
        pm=context.getPackageManager();
        mApps=mList;
    }
    public void setRecords(List<AppBean> records){
        this.mApps =records;
    }
    public void add(AppBean info){
        this.mApps.add(info);
    }

    @Override
    public int getCount() {
        return mApps ==null?0: mApps.size();
    }

    @Override
    public Object getItem(int position) {
        return mApps ==null?null: mApps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder=null;
            if(convertView==null){
                holder=new ViewHolder();
                convertView=mInflater.inflate(R.layout.app_list_layout,null);
                bindView(convertView,holder);
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            }
            renderView(position,holder);
            return convertView;

    }
    public void bindView(View convertView,ViewHolder holder){
        holder.icon=convertView.findViewById(R.id.appicon);

        holder.appname=convertView.findViewById(R.id.appname);

    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void renderView(int position, ViewHolder holder){
        AppBean app= mApps.get(position);

        holder.icon.setBackground(app.getIcon());

        holder.appname.setText(app.getAppname());


    }

    class ViewHolder{
        public ImageView icon;

        public TextView appname;

    }
}
