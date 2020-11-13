package com.rom471.ui.fragments2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rom471.adapter.OneUseAdapter;
import com.rom471.db2.AppRecordsRepository;
import com.rom471.db2.OneUse;

import com.rom471.utils.DBUtils;

import java.util.List;

public abstract class OneUseFindFragment extends Fragment implements View.OnClickListener {
    RecyclerView list_view;
    //RecordDBHelper db;
    int laytoutResource;
    int listviewResource;
    AppRecordsRepository appOneUsesRepository;
    OneUseAdapter mAdapter;
    List<OneUse> mOneUses;
    Context context;
    public OneUseFindFragment(@LayoutRes int layoutResource, @IdRes int listviewResouce){
        this.laytoutResource=layoutResource;
        this.listviewResource=listviewResouce;
    }
    public OneUseFindFragment(){

    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(laytoutResource,container,false);
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bindView();
        context=getContext();
        list_view=getActivity().findViewById(listviewResource);
        registRecords();
        mAdapter=new OneUseAdapter(mOneUses);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list_view.setLayoutManager(layoutManager);

        list_view.setAdapter(mAdapter);
        //设置动画
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setRemoveDuration(600);
        list_view.setItemAnimator(defaultItemAnimator);
        registerForContextMenu(list_view);

    }
    abstract void bindView();
    private void registRecords(){
        appOneUsesRepository=new AppRecordsRepository(getActivity().getApplication());
        appOneUsesRepository.getAllOneUsesLive().observe(this,new Observer<List<OneUse>>(){
            @Override
            public void onChanged(List<OneUse> records) {
                mOneUses=records;
                DBUtils.setOneUseIcon(context,mOneUses);
                mAdapter.setOneUses(mOneUses);
                //list_view.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
    void updateWithFoundRecords(List<OneUse> records){
        DBUtils.setOneUseIcon(context,records);
        mAdapter.setOneUses(records);
        list_view.setAdapter(mAdapter);
    }
    @Override
    public abstract void onClick(View v);

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position=mAdapter.getPosition();
        OneUse record=mOneUses.get(position);

        switch (item.getItemId()){
            case 0:
                appOneUsesRepository.delete(record);
                Toast.makeText(context,"记录已经删除",Toast.LENGTH_SHORT).show();
//                mAdapter.notifyDataSetChanged();
//                mAdapter.notifyItemRemoved(position);
//                mAdapter.notifyItemRangeChanged(position,mRecords.size()-position);
                break;
        }
        return super.onContextItemSelected(item);
    }
}
