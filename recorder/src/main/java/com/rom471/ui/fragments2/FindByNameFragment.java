package com.rom471.ui.fragments2;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rom471.db.Record;
import com.rom471.recorder.R;

import java.util.ArrayList;
import java.util.List;

public class FindByNameFragment extends RecordFindFragment{
    Button record_search_btn;
    EditText record_search_et;
    TextView record_result_tv;
    public FindByNameFragment(){
        super(R.layout.main_fragment_record_find_by_name,R.id.record_list_by_name);
    }

    @Override
    void bindView() {
        record_search_btn=getActivity().findViewById(R.id.record_search_btn);
        record_search_et=getActivity().findViewById(R.id.record_search_et);
        record_result_tv =getActivity().findViewById(R.id.record_serch_result);

        record_search_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.record_search_btn:
                String appname=record_search_et.getText().toString();
                List<Record> records = filterByName(appname, mRecords);
                updateWithFoundRecords(records);
                record_result_tv.setText("查到记录："+records.size()+"条");
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
