package com.rom471.db;


import android.content.Context;
import android.content.pm.ApplicationInfo;
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

import com.rom471.recorder.R;

import java.util.ArrayList;
import java.util.List;

public class AppListViewAdapter extends BaseAdapter {
    public static final String TAG= AppListViewAdapter.class.getSimpleName();
    private List<Record> appInfoList;

    private LayoutInflater mInflater;
    PackageManager pm;


    public AppListViewAdapter(Context context){
        appInfoList =new ArrayList<>();

        mInflater=LayoutInflater.from(context);
        pm=context.getPackageManager();
    }
    public AppListViewAdapter(Context context, List<Record> mList){
        this.appInfoList =mList;
        mInflater=LayoutInflater.from(context);
        pm=context.getPackageManager();
    }

    public void add(Record info){
        this.appInfoList.add(info);
    }

    @Override
    public int getCount() {
        return appInfoList ==null?0: appInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return appInfoList ==null?null: appInfoList.get(position);
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
        holder.id=convertView.findViewById(R.id.id);
        holder.appname=convertView.findViewById(R.id.appname);
        holder.first_run_time=convertView.findViewById(R.id.first_run_time);
        holder.last_run_time=convertView.findViewById(R.id.last_run_time);
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void renderView(int position, ViewHolder holder){
        Record appinfo= appInfoList.get(position);
        Drawable icon=null;
        holder.icon.setBackground(icon);
        holder.id.setText(""+position);
        holder.appname.setText(appinfo.getAppname());
        holder.first_run_time.setText(appinfo.getDatatime());
        holder.last_run_time.setText(""+appinfo.getNet());

    }

    class ViewHolder{
        public ImageView icon;
        public TextView id;
        public TextView appname;
        public TextView first_run_time;
        public TextView last_run_time;
        public TextView total_run_time;
    }
}
