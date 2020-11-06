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
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rom471.adapter.RecordsAdapter;
import com.rom471.adapter.RecordsViewModel;
import com.rom471.db.Record;
import com.rom471.recorder.R;
import com.rom471.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;

public class FindByNameFragment extends Fragment implements View.OnClickListener {
    RecyclerView list_view;
    //RecordDBHelper db;

    RecordsAdapter mAdapter;
    Button record_search_btn;

    List<Record> mRecords;
    EditText record_search_et;
    TextView record_result_tv;
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
//        RecordsViewModelFactory recordsViewModelFactory=new RecordsViewModelFactory();
        //recordsViewModel= new ViewModelProvider(getActivity()).get(RecordsViewModel.class);


        context=getContext();
        list_view=getActivity().findViewById(R.id.record_list_by_name);

        registRecords();


        mAdapter=new RecordsAdapter(mRecords);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list_view.setLayoutManager(layoutManager);
        list_view.setAdapter(mAdapter);

        record_search_btn=getActivity().findViewById(R.id.record_search_btn);
        record_search_et=getActivity().findViewById(R.id.record_search_et);
        record_result_tv =getActivity().findViewById(R.id.record_serch_result);

        record_search_btn.setOnClickListener(this);

    }
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
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.record_search_btn:
                String appname=record_search_et.getText().toString();
                List<Record> records = filterByName(appname, mRecords);
                mAdapter.setRecords(records);
                record_result_tv.setText("查到记录："+records.size()+"条");
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
