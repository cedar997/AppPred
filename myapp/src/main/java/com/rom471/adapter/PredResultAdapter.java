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
import com.rom471.db2.OnePred;
import com.rom471.recorder.R;

import java.util.ArrayList;
import java.util.List;

public class PredResultAdapter extends RecyclerView.Adapter<PredResultAdapter.ViewHolder> {

    private List<OnePred> mAppsList;

    Fragment context;
    AppRecordsRepository appRecordsRepository;


    public void setmAppsList(List<OnePred> mAppsList) {
        this.mAppsList = mAppsList;
    }
    public PredResultAdapter(){
        this.context=context;
        this.appRecordsRepository=appRecordsRepository;
        mAppsList=new ArrayList<>();

    }


    public void add(OnePred onePred){
        mAppsList.add(onePred);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.pred_result_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int top1=0;
        int top3=0;
        int top5=0;
        if(position==0){
            for(OnePred onePred:mAppsList){
                top1+=onePred.getTop1();
                top3+=onePred.getTop3();
                top5+=onePred.getTop5();
            }
            holder.top1.setText(String.format("top1:%d /%d", top1,mAppsList.size()));
            holder.top3.setText(String.format("top3:%d /%d", top3,mAppsList.size()));
            holder.top5.setText(String.format("top5:%d /%d", top5,mAppsList.size()));

        }


    }

    @Override
    public int getItemCount() {
        if(mAppsList!=null)
            return mAppsList.size();
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView top1;
        TextView top3;
        TextView top5;

        public ViewHolder (View view)
        {
            super(view);

            top1 = (TextView) view.findViewById(R.id.top1_rate);
            top3 = (TextView) view.findViewById(R.id.top3_rate);
            top5 = (TextView) view.findViewById(R.id.top5_rate);
        }

    }
}
