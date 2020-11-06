package com.rom471.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.record_list_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Record app= mRecordsList.get(position);
        holder.appIcon.setBackground(app.getIcon());
        holder.appName.setText(app.getAppname());
        holder.spendTime.setText(app.getTimeSpendString());
        holder.timeStamp.setText(app.getDatatime());
        holder.battery.setText(""+app.getBattery());
        holder.charging.setText(app.getChargingString());
        holder.net.setText(app.getNetString());
    }

    @Override
    public int getItemCount() {
        if( mRecordsList==null)
            return 0;
        return mRecordsList.size();
    }

    public void setRecords(List<Record> filterByName) {
        this.mRecordsList=filterByName;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView appIcon;
        TextView id;
        TextView appName;
        TextView spendTime;
        TextView timeStamp;
         TextView battery;
         TextView charging;
         TextView net;

        public ViewHolder (View view)
        {
            super(view);
            appIcon = (ImageView) view.findViewById(R.id.appicon_img);
            id = (TextView) view.findViewById(R.id.id_tv);
            appName = (TextView) view.findViewById(R.id.appname_tv);
            spendTime = (TextView) view.findViewById(R.id.spendtime_tv);
            timeStamp = (TextView) view.findViewById(R.id.timestamp_tv);
            battery = (TextView) view.findViewById(R.id.battery_tv);
            charging = (TextView) view.findViewById(R.id.charging_tv);
           net = (TextView) view.findViewById(R.id.net_tv);
        }

    }
}
