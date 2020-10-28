package com.rom471.db;


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

import com.rom471.recorder.R;

import java.util.ArrayList;
import java.util.List;

public class RecordListViewAdapter extends BaseAdapter {
    public static final String TAG= RecordListViewAdapter.class.getSimpleName();
    private List<Record> mRecords;

    private LayoutInflater mInflater;
    PackageManager pm;


    public RecordListViewAdapter(Context context){
        mRecords =new ArrayList<>();

        mInflater=LayoutInflater.from(context);
        pm=context.getPackageManager();
    }
    public RecordListViewAdapter(Context context, List<Record> mList){
        this.mRecords =mList;
        mInflater=LayoutInflater.from(context);
        pm=context.getPackageManager();
    }
    public void setRecords(List<Record> records){
        this.mRecords=records;
    }
    public void add(Record info){
        this.mRecords.add(info);
    }

    @Override
    public int getCount() {
        return mRecords ==null?0: mRecords.size();
    }

    @Override
    public Object getItem(int position) {
        return mRecords ==null?null: mRecords.get(position);
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
                convertView=mInflater.inflate(R.layout.record_list_layout,null);
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
        holder.battery =convertView.findViewById(R.id.battery_tv);
        holder.power =convertView.findViewById(R.id.power_tv);
        holder.net=convertView.findViewById(R.id.net_tv);
        holder.time=convertView.findViewById(R.id.time_tv);
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void renderView(int position, ViewHolder holder){
        Record record= mRecords.get(position);
        Drawable icon=null;
        holder.icon.setBackground(icon);
        holder.id.setText(""+position);
        holder.appname.setText(record.getAppname());
        holder.battery.setText(""+record.getBattery());
        holder.power.setText(record.getChargingString());
        holder.net.setText(record.getNetString());
        holder.time.setText(record.getDatatime());

    }

    class ViewHolder{
        public ImageView icon;
        public TextView id;
        public TextView appname;
        public TextView battery;
        public TextView power;
        public TextView net;
        public TextView time;
    }
}
