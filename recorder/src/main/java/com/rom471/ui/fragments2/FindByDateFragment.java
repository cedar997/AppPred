package com.rom471.ui.fragments2;

import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.rom471.adapter.RecordsAdapter;
import com.rom471.db.Record;
import com.rom471.db.RecordDAO;
import com.rom471.db.RecordDataBase;
import com.rom471.recorder.R;
import com.rom471.utils.DBUtils;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class FindByDateFragment extends Fragment  implements View.OnClickListener {
   RecyclerView list_view;
    //RecordDBHelper db;
    RecordDataBase recordDataBase;
    RecordDAO recordDAO;
    RecordsAdapter mAdapter;

    List<Record> mRecords;
    Button start_date_btn;
    Button end_date_btn;
    Button start_btn;
    TextView start_date_tv;
    TextView end_date_tv;
    TextView result_tv;

    long start_timestamp;
    long end_timestamp;
    Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment_record_find_by_date,container,false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context=getContext();
        bindView();
        start_date_btn.setOnClickListener(this);
        end_date_btn.setOnClickListener(this);
        start_btn.setOnClickListener(this);
        recordDataBase= Room.databaseBuilder(context, RecordDataBase.class, "records.db").allowMainThreadQueries().build();
        recordDAO=recordDataBase.getRecordDAO();
        mRecords = recordDAO.getRecords(100);
        DBUtils.setAppIcon(context,mRecords);
        mAdapter=new RecordsAdapter(mRecords);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list_view.setLayoutManager(layoutManager);
        list_view.setAdapter(mAdapter);
    }
    private void bindView(){
        list_view=getActivity().findViewById(R.id.record_list_by_date);
        start_date_btn=getActivity().findViewById(R.id.record_date_start_btn);
        end_date_btn=getActivity().findViewById(R.id.record_date_end_btn);
        start_btn=getActivity().findViewById(R.id.date_start_search);
        start_date_tv=getActivity().findViewById(R.id.record_date_start_tv);
        end_date_tv=getActivity().findViewById(R.id.record_date_end_tv);
        result_tv=getActivity().findViewById(R.id.date_search_result);
    }

    private List<Record> filterByDate( List<Record> old_list,long start,long end){
        List<Record> new_list=new ArrayList<>();
        for (Record r:old_list
             ) {
            if (r.getTimeStamp()>start && r.getTimeStamp()<end){
                new_list.add(r);
            }
        }
        return  new_list;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.record_date_start_btn:
               showDatePickerDialog(context,true  );
                break;
            case R.id.record_date_end_btn:
                showDatePickerDialog(context,false);
                break;
            case R.id.date_start_search:
                List<Record> records = filterByDate(mRecords, start_timestamp, end_timestamp);
                result_tv.setText("查到记录："+records.size()+"条");
                mAdapter.setRecords(records);
                list_view.setAdapter(mAdapter);

                break;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public  void showDatePickerDialog(Context activity,boolean start) {
        Calendar  calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                // 此处得到选择的时间，可以进行你想要的操作
                long timestamp = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTimeInMillis();
                if(start){
                    start_timestamp=timestamp;
                    start_date_tv.setText(""+year+"年"+monthOfYear+"月"+dayOfMonth+"日");
                }
                else {
                    end_timestamp=timestamp;
                    end_date_tv.setText(""+year+"年"+monthOfYear+"月"+dayOfMonth+"日");

                }
            }
        }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }
}
