package com.rom471.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rom471.db2.App;
import com.rom471.recorder.R;

import java.util.List;

public abstract class AppsAdapter1 extends RecyclerView.Adapter<AppsAdapter1.ViewHolder> {
    private List<App> mAppsList;

    public List<App> getmAppsList() {
        return mAppsList;
    }

    public void setmAppsList(List<App> mAppsList) {
        this.mAppsList = mAppsList;
    }

    public AppsAdapter1(List<App> mAppsList) {
        this.mAppsList = mAppsList;
    }
    public AppsAdapter1() {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_app_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        App app=mAppsList.get(position);
        holder.appIcon.setBackground(app.getIcon());
        holder.appName.setText(app.getAppName());

        setAppInfo(holder.appInfo,app);
    }
    //设置
    public abstract void setAppInfo(TextView appInfo,App app);
    @Override
    public int getItemCount() {
        if(mAppsList!=null)
        return mAppsList.size();
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView appIcon;
        TextView appName;
        TextView appInfo;

        public ViewHolder (View view)
        {
            super(view);
            appIcon = (ImageView) view.findViewById(R.id.appicon);
            appName = (TextView) view.findViewById(R.id.appname);
            appInfo = (TextView) view.findViewById(R.id.app_info);
        }

    }
}
