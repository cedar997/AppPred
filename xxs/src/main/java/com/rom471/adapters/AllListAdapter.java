package com.rom471.adapters;


import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.rom471.lab1.R;
import com.rom471.appsdb.AppBean;

import java.util.ArrayList;
import java.util.List;

public class AllListAdapter extends BaseAdapter implements Filterable {
    public static final String TAG= AllListAdapter.class.getSimpleName();
    private List<AppBean> appList;
    private List<AppBean> allAppList;

    private LayoutInflater mInflater;
    PackageManager pm;


    public AllListAdapter(Context context){
        appList =new ArrayList<>();

        mInflater=LayoutInflater.from(context);
        pm=context.getPackageManager();
    }


    public void add(AppBean app){
        this.appList.add(app);
    }

    @Override
    public int getCount() {
        return appList ==null?0: appList.size();
    }

    @Override
    public Object getItem(int position) {
        return appList ==null?null: appList.get(position);
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
        holder.liked =convertView.findViewById(R.id.liked);
        holder.appname=convertView.findViewById(R.id.appname);

    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void renderView(int position, ViewHolder holder){
        AppBean appinfo= appList.get(position);
        if(appinfo.isLiked()){
            holder.liked.setBackgroundResource(R.drawable.stared);
        }else {
            holder.liked.setBackgroundResource(R.drawable.star);
        }
        Drawable icon=appinfo.getIcon();
        holder.icon.setBackground(icon);
        holder.appname.setText(appinfo.getAppname());

    }

    public AppBean get(int position) {
        return appList.get(position);
    }

    @Override
    public Filter getFilter() {
        if(allAppList==null){
            allAppList=appList;
        }
        Filter filter=new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<AppBean> filteredApps = new ArrayList<>();


                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < allAppList.size(); i++) {
                    AppBean app = allAppList.get(i);
                    if (app.getAppname().toLowerCase().contains(constraint.toString()))  {
                        filteredApps.add(app);
                    }
                }
                results.count =filteredApps.size();
                results.values = filteredApps;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                appList = (List<AppBean>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    class ViewHolder{
        public ImageView icon;
        public ImageView liked;
        public TextView appname;

    }

}
