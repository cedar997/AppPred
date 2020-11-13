package com.rom471.ui.fragments;

import androidx.fragment.app.Fragment;
import android.content.Context;
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
import com.rom471.db2.App;
import com.rom471.db2.AppDao;
import com.rom471.db2.AppDataBase;
import com.rom471.db2.OnePred;
import com.rom471.db2.OneUse;
import com.rom471.pred.MyPredicter;
import com.rom471.recorder.R;
import com.rom471.utils.Const;

import java.util.List;

public class SortFragment extends Fragment implements View.OnClickListener {
    RecyclerView recyclerView;
    PredAdapter predAdapter;
    MyPredicter predicter;
    RecyclerView pred_app_top_5;
    List<App>  appLists;
    LiveData<List<OneUse>> useLists;
    Context context;

    AppDao appDao;
    Button sortBtn;

    //RecordDBHelper db;
    AppSortAdapter adapter_last;




    public static final int APP_LIST_SIZE=100;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment_sort,container,false);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context=getContext();

        recyclerView =getActivity().findViewById(R.id.app_list);


        sortBtn =getActivity().findViewById(R.id.sort_btn);


        sortBtn.setOnClickListener(this);


        AppDataBase appDataBase= AppDataBase.getInstance(context);
        appDao =appDataBase.getAppDao();
        appDataBase=AppDataBase.getInstance(getActivity().getApplication());
        appDao=appDataBase.getAppDao();

        //

        adapter_last =new AppSortAdapter(this,appDao);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(context);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager1);
//
        recyclerView.setAdapter(adapter_last);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(context);
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);


        LinearLayoutManager layoutManager3= new LinearLayoutManager(context);
        layoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);

        LinearLayoutManager layoutManager4= new LinearLayoutManager(context);
        layoutManager4.setOrientation(LinearLayoutManager.HORIZONTAL);
        initPredView();
    }
    private void initPredView(){
        predicter=new MyPredicter(getActivity().getApplication());
        predAdapter=new PredAdapter();
        pred_app_top_5=getActivity().findViewById(R.id.app_pred);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(context);
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        pred_app_top_5.setLayoutManager(layoutManager2);
//
        pred_app_top_5.setAdapter(predAdapter);
    }
    @Override
    public void onResume() {
        super.onResume();
//        List<OnePred> aLlOnePreds = appDao.getALlOnePreds();
//        OnePred onePred = predicter.getOnePred();
        predicter.updateAdapter(predAdapter);
        //上传当前app名字到服务器，并获得推荐




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.sort_btn:

                PopupMenu popup = new PopupMenu(getActivity(), sortBtn);

                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.popup_one:
                                adapter_last.change(Const.CHANGE_LAST_USE);
                                sortBtn.setText(R.string.sort_1);
                                break;
                            case R.id.popup_two:
                                adapter_last.change(Const.CHAGE_TIME_MOST);
                                sortBtn.setText(R.string.sort_2);
                                break;
                            case R.id.popup_three:
                                adapter_last.change(Const.CHANGE_COUNT_MOST);
                                sortBtn.setText(R.string.sort_3);
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
