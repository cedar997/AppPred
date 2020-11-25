package com.rom471.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import com.rom471.db2.App;
import com.rom471.db2.AppDao;
import com.rom471.recorder.R;
import com.rom471.utils.Const;
import com.rom471.utils.DBUtils;
import java.util.List;

public class AppSortAdapter extends RecyclerView.Adapter<AppSortAdapter.ViewHolder> {

    private List<App> mAppsList;
    public static final int APP_LIST_SIZE=100;
    LiveData<List<App>> liveData;
    Fragment context;
    AppDao appDao;
    int type;


    public void setmAppsList(List<App> mAppsList) {
        this.mAppsList = mAppsList;
    }



    public AppSortAdapter(Fragment context, AppDao appDao){
        this.context=context;
        this.appDao=appDao;
        change(Const.CHANGE_LAST_USE);

    }

    public void change(int type){
        this.type=type;
        switch (type){
            case Const.CHAGE_TIME_MOST:
                liveData= appDao.getMostUsedApps(APP_LIST_SIZE);
                break;
            case Const.CHANGE_LAST_USE:
                liveData= appDao.getLastUsedApp(APP_LIST_SIZE);
                break;
            case Const.CHANGE_COUNT_MOST:
                liveData= appDao.getMostCountsApps(APP_LIST_SIZE);
                break;

        }
        liveData.observe( context, new Observer<List<App>>() {
            @Override
            public void onChanged(List<App> apps) {
                DBUtils.setAppsIcon(context.getContext(),apps);
                setmAppsList(apps);
                notifyDataSetChanged();
            }
        });
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sort_app_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        App app=mAppsList.get(position);
        holder.appIcon.setBackground(app.getIcon());
        holder.appName.setText(app.getAppName());
        holder.appRank.setText(""+(position+1));
        switch (type){
            case Const.CHAGE_TIME_MOST:
                holder.appInfo.setText(DBUtils.getTimeSpendString(app.getTotalRuningTime()) );
                break;
            case Const.CHANGE_LAST_USE:
                holder.appInfo.setText(DBUtils.getSinceTimeString(app.getLastRuningTime())+"前");
                break;
            case Const.CHANGE_COUNT_MOST:
                holder.appInfo.setText(app.getUseCount()+"次");
                break;
        }
    }

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
        TextView appRank;

        public ViewHolder (View view)
        {
            super(view);
            appIcon = (ImageView) view.findViewById(R.id.appicon);
            appName = (TextView) view.findViewById(R.id.appname);
            appInfo = (TextView) view.findViewById(R.id.app_info);
            appRank = (TextView) view.findViewById(R.id.app_rank);
        }

    }
}
