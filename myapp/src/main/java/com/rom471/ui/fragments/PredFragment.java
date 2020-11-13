package com.rom471.ui.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rom471.adapter.AppSortAdapter;
import com.rom471.adapter.PredAdapter;
import com.rom471.adapter.PredResultAdapter;
import com.rom471.db2.App;
import com.rom471.db2.AppDao;
import com.rom471.db2.AppDataBase;
import com.rom471.db2.OnePred;
import com.rom471.db2.OneUse;
import com.rom471.db2.SimpleApp;
import com.rom471.net.DataSender;
import com.rom471.pred.MyPredicter;
import com.rom471.recorder.R;
import com.rom471.utils.AppUtils;
import com.rom471.utils.Const;

import java.util.Arrays;
import java.util.List;

public class PredFragment extends Fragment implements View.OnClickListener {

    RecyclerView pred_app_top_5;
    RecyclerView pred_app_result;

    List<App>  appLists;
    LiveData<List<OneUse>> useLists;
    Context context;

    AppDao appDao;

    RecyclerView pred_app_from_server;
    //RecordDBHelper db;

    PredAdapter predAdapter;
    PredAdapter predServerAdapter;
    PredResultAdapter predResultAdapter;
    MyPredicter predicter;

    public static final int APP_LIST_SIZE=100;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment_pred,container,false);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context=getContext();


        pred_app_top_5=getActivity().findViewById(R.id.app_pred);
        pred_app_result=getActivity().findViewById(R.id.app_pred_result);
        pred_app_from_server =getActivity().findViewById(R.id.app_pred_from_server);



        AppDataBase appDataBase= AppDataBase.getInstance(context);
        appDao =appDataBase.getAppDao();
        appDataBase=AppDataBase.getInstance(getActivity().getApplication());
        appDao=appDataBase.getAppDao();

        //


        predicter=new MyPredicter(getActivity().getApplication());
        predAdapter=new PredAdapter();
        predServerAdapter=new PredAdapter();
        predResultAdapter=new PredResultAdapter();
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(context);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);


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
        LinearLayoutManager layoutManager4= new LinearLayoutManager(context);
        layoutManager4.setOrientation(LinearLayoutManager.HORIZONTAL);
        pred_app_from_server.setLayoutManager(layoutManager4);
        pred_app_from_server.setAdapter(predServerAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        OnePred onePred = predicter.getOnePred();
        List<OnePred> aLlOnePreds = appDao.getALlOnePreds();
        predResultAdapter.setmAppsList(aLlOnePreds);
        predResultAdapter.notifyDataSetChanged();
        predicter.updateAdapter(predAdapter);
        //上传当前app名字到服务器，并获得推荐
        SimpleApp currentApp=appDao.getCurrentApp();
        Handler handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {

                String s = msg.getData().getString("value");
                StringBuilder sb=new StringBuilder();
                List<String> converted = Arrays.asList(s.split(",", -1));

                List<SimpleApp> simpleAppsFromNames = AppUtils.getSimpleAppsFromNames(converted);
                predServerAdapter.setmAppsList(simpleAppsFromNames);
                predServerAdapter.notifyDataSetChanged();
            }
        };
        if(currentApp!=null){
            new Thread(()->{
                String ret = DataSender.send(currentApp);
                if(ret!=null) {
                    Bundle data = new Bundle();
                    data.putString("value", ret);
                    Message message = new Message();
                    message.setData(data);
                    handler.sendMessage(message);
                }
            }).start();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){


        }
    }
}
