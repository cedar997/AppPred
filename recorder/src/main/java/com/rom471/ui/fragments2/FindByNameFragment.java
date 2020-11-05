package com.rom471.ui.fragments2;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.rom471.adapter.RecordsAdapter;
import com.rom471.db.Record;
import com.rom471.recorder.R;
import com.rom471.db.RecordDAO;
import com.rom471.db.RecordDataBase;
import com.rom471.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;

public class FindByNameFragment extends Fragment implements View.OnClickListener {
    RecyclerView list_view;
    //RecordDBHelper db;
    RecordDataBase recordDataBase;
    RecordDAO recordDAO;
    RecordsAdapter mAdapter;
    Button record_search_btn;
    Button record_update_btn;
    List<Record> mRecords;
    EditText record_search_et;
    TextView record_stat_tv;
    Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment_record_find_by_name,container,false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context=getContext();
        list_view=getActivity().findViewById(R.id.record_list_by_name);
        recordDataBase= Room.databaseBuilder(context, RecordDataBase.class, "records.db").allowMainThreadQueries().build();
        recordDAO=recordDataBase.getRecordDAO();
        mRecords = recordDAO.getRecords(100);
        DBUtils.setAppIcon(context,mRecords);
        mAdapter=new RecordsAdapter(mRecords);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list_view.setLayoutManager(layoutManager);
        list_view.setAdapter(mAdapter);

        record_search_btn=getActivity().findViewById(R.id.record_search_btn);
        record_search_et=getActivity().findViewById(R.id.record_search_et);
        record_stat_tv=getActivity().findViewById(R.id.record_stat_tv);
        record_update_btn=getActivity().findViewById(R.id.record_update_btn);
        record_search_btn.setOnClickListener(this);
        record_update_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.record_search_btn:
                String appname=record_search_et.getText().toString();
                mAdapter.setRecords(filterByName(appname,mRecords));
                list_view.setAdapter(mAdapter);
                break;
            case R.id.record_update_btn:
                //mRecords = db.queryLast(500);
                mRecords=recordDAO.getRecords(100);
                mAdapter.setRecords(mRecords);
                list_view.setAdapter(mAdapter);
                break;
        }
    }
    private List<Record> filterByName(String appname, List<Record> old_list){
        List<Record> new_list=new ArrayList<>();
        for (Record r:old_list
             ) {
            if (r.getAppname().toLowerCase().contains(appname.toLowerCase())){
                new_list.add(r);
            }
        }
        return  new_list;
    }
}
