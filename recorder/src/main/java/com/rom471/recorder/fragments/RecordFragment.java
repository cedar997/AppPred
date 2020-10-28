package com.rom471.recorder.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.rom471.db.Record;
import com.rom471.db.RecordDBHelper;
import com.rom471.db.RecordListViewAdapter;
import com.rom471.recorder.R;

import java.util.List;

public class RecordFragment extends Fragment implements View.OnClickListener {
    ListView list_view;
    RecordDBHelper db;
    RecordListViewAdapter mAdapter;
    Button record_search_btn;
    EditText record_search_et;
    Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_record_fragment,container,false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context=getContext();
        list_view=getActivity().findViewById(R.id.record_list);
        db=new RecordDBHelper(getContext(),"app.db");
        List<Record> records = db.queryAll();
        mAdapter=new RecordListViewAdapter(getContext(),records);
        list_view.setAdapter(mAdapter);
        record_search_btn=getActivity().findViewById(R.id.record_search_btn);
        record_search_et=getActivity().findViewById(R.id.record_search_et);
        record_search_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.record_search_btn:
                String appname=record_search_et.getText().toString();
                List<Record> records = db.queryAllByName(appname);
                mAdapter=new RecordListViewAdapter(context,records);
                list_view.setAdapter(mAdapter);
                break;
        }
    }
}
