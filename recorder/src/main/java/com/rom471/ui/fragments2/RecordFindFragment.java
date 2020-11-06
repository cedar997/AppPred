package com.rom471.ui.fragments2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rom471.adapter.RecordsAdapter;
import com.rom471.adapter.RecordsViewModel;
import com.rom471.db.Record;
import com.rom471.utils.DBUtils;

import java.util.List;

public abstract class RecordFindFragment extends Fragment implements View.OnClickListener {
    RecyclerView list_view;
    //RecordDBHelper db;
    int laytoutResource;
    int listviewResource;
    RecordsAdapter mAdapter;
    List<Record> mRecords;
    Context context;
    public RecordFindFragment(@LayoutRes int layoutResource,@IdRes int listviewResouce){
        this.laytoutResource=layoutResource;
        this.listviewResource=listviewResouce;
    }
    public RecordFindFragment(){

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
        mAdapter=new RecordsAdapter(mRecords);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list_view.setLayoutManager(layoutManager);
        list_view.setAdapter(mAdapter);

    }
    abstract void bindView();
    private void registRecords(){
        RecordsViewModel recordsViewModel;
        recordsViewModel=new RecordsViewModel(getActivity().getApplication());
        recordsViewModel.getAllRecords().observe(this,new Observer<List<Record>>(){
            @Override
            public void onChanged(List<Record> records) {
                mRecords=records;
                DBUtils.setAppIcon(context,mRecords);
                mAdapter.setRecords(mRecords);
                list_view.setAdapter(mAdapter);
            }
        });
    }
    void updateWithFoundRecords(List<Record> records){
        DBUtils.setAppIcon(context,records);
        mAdapter.setRecords(records);
        list_view.setAdapter(mAdapter);
    }
    @Override
    public abstract void onClick(View v);
}
