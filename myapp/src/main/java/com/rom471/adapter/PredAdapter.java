package com.rom471.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.rom471.db2.AppRecordsRepository;
import com.rom471.db2.SimpleApp;
import com.rom471.recorder.R;

import java.util.List;

public class PredAdapter extends RecyclerView.Adapter<PredAdapter.ViewHolder> {

    private List<SimpleApp> mAppsList;

    Fragment context;
    AppRecordsRepository appRecordsRepository;


    public void setmAppsList(List<SimpleApp> mAppsList) {
        this.mAppsList = mAppsList;
    }
    public PredAdapter(){
        this.context=context;
        this.appRecordsRepository=appRecordsRepository;


    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.pred_app_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position>=5) {
            return;
        }
        SimpleApp app=mAppsList.get(position);
        if(app==null) return;

            holder.appInfo.setText("权重:"+(app.getWeight()));

        holder.appIcon.setBackground(app.getIcon());
        holder.appName.setText(app.getAppName());

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

        public ViewHolder (View view)
        {
            super(view);
            appIcon = (ImageView) view.findViewById(R.id.appicon);
            appName = (TextView) view.findViewById(R.id.appname);
            appInfo = (TextView) view.findViewById(R.id.app_info);
        }

    }
}
