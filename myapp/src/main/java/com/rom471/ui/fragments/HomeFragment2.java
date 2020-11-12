package com.rom471.ui.fragments;

import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rom471.adapter.AppSortAdapter;

import com.rom471.adapter.PredAdapter;
import com.rom471.adapter.PredResultAdapter;
import com.rom471.db2.App;
import com.rom471.db2.AppDao;
import com.rom471.db2.AppDataBase;
import com.rom471.db2.AppRecordsRepository;
import com.rom471.db2.OnePred;
import com.rom471.db2.OneUse;
import com.rom471.pred.MyPredicter;
import com.rom471.recorder.R;
import com.rom471.utils.Const;

import java.util.List;

public class HomeFragment2 extends Fragment implements View.OnClickListener {
    RecyclerView recyclerView;
    RecyclerView pred_app_top_5;
    RecyclerView pred_app_result;

    List<App>  appLists;
    LiveData<List<OneUse>> useLists;
    Context context;
    AppRecordsRepository appRecordsRepository;
    AppDao appDao;
    Button total_time_btn;

    //RecordDBHelper db;
    AppSortAdapter adapter_last;
    PredAdapter predAdapter;
    PredResultAdapter predResultAdapter;
    MyPredicter predicter;

    public static final int APP_LIST_SIZE=100;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment_home,container,false);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context=getContext();

        recyclerView =getActivity().findViewById(R.id.app_list);
        pred_app_top_5=getActivity().findViewById(R.id.app_pred);
        pred_app_result=getActivity().findViewById(R.id.app_pred_result);

        total_time_btn=getActivity().findViewById(R.id.sort_btn);


        total_time_btn.setOnClickListener(this);


        AppDataBase appDataBase= AppDataBase.getInstance(context);
        appDao =appDataBase.getAppDao();

        appRecordsRepository=new AppRecordsRepository(getActivity().getApplication());
        //

        adapter_last =new AppSortAdapter(this,appRecordsRepository);
        predicter=new MyPredicter(getActivity().getApplication());
        predAdapter=new PredAdapter();
        predResultAdapter=new PredResultAdapter();
        OnePred onePred = new OnePred();
        onePred.setTop1(1);
        predResultAdapter.add(onePred);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(context);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager1);
//
        recyclerView.setAdapter(adapter_last);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(context);
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        pred_app_top_5.setLayoutManager(layoutManager2);
//
        pred_app_top_5.setAdapter(predAdapter);

        LinearLayoutManager layoutManager3= new LinearLayoutManager(context);
        layoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        pred_app_result.setLayoutManager(layoutManager3);
//
        pred_app_result.setAdapter(predResultAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        OnePred onePred = predicter.getOnePred();
        List<OnePred> aLlOnePreds = appDao.getALlOnePreds();
        predResultAdapter.setmAppsList(aLlOnePreds);
        predResultAdapter.notifyDataSetChanged();
        predicter.updateAdapter(predAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.sort_btn:

                PopupMenu popup = new PopupMenu(getActivity(), total_time_btn);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.popup_one:
                                adapter_last.change(Const.CHANGE_LAST_USE);
                                break;
                            case R.id.popup_two:
                                adapter_last.change(Const.CHAGE_TIME_MOST);
                                break;
                            case R.id.popup_three:
                                adapter_last.change(Const.CHANGE_COUNT_MOST);
                                break;
                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
                break;
        }
    }
}
