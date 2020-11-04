package com.rom471.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rom471.db.Record;
import com.rom471.recorder.R;

import java.util.List;

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.ViewHolder> {
    private List<Record> mRecordsList;

    public RecordsAdapter(List<Record> mRecordsList) {
        this.mRecordsList = mRecordsList;
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
        Record app= mRecordsList.get(position);
        holder.appIcon.setBackground(app.getIcon());
        holder.appName.setText(app.getAppname());
        holder.spendTime.setText(app.getTimeSpendString());
    }

    @Override
    public int getItemCount() {
        return mRecordsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView appIcon;
        TextView appName;
        TextView spendTime;

        public ViewHolder (View view)
        {
            super(view);
            appIcon = (ImageView) view.findViewById(R.id.appicon);
            appName = (TextView) view.findViewById(R.id.appname);
            spendTime = (TextView) view.findViewById(R.id.spend_time);
        }

    }
}
