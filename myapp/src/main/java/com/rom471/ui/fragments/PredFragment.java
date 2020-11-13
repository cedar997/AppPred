package com.rom471.ui.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rom471.adapter.PredAdapter;
import com.rom471.db2.App;
import com.rom471.db2.AppDao;
import com.rom471.db2.AppDataBase;
import com.rom471.db2.OneUse;
import com.rom471.db2.SimpleApp;
import com.rom471.net.DataSender;
import com.rom471.recorder.R;
import com.rom471.utils.AppUtils;

import java.util.Arrays;
import java.util.List;

public class PredFragment extends Fragment implements View.OnClickListener {




    List<App>  appLists;
    LiveData<List<OneUse>> useLists;
    Context context;

    AppDao appDao;
    WebView webView;
    RecyclerView pred_app_from_server;
    //RecordDBHelper db;


    PredAdapter predServerAdapter;



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
        pred_app_from_server =getActivity().findViewById(R.id.app_pred_from_server);


        AppDataBase appDataBase= AppDataBase.getInstance(context);
        appDao =appDataBase.getAppDao();
        appDataBase=AppDataBase.getInstance(getActivity().getApplication());
        appDao=appDataBase.getAppDao();

        //




        predServerAdapter=new PredAdapter(getActivity());

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(context);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);





        LinearLayoutManager layoutManager3= new LinearLayoutManager(context);
        layoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);

//

        LinearLayoutManager layoutManager4= new LinearLayoutManager(context);
        layoutManager4.setOrientation(LinearLayoutManager.HORIZONTAL);
        pred_app_from_server.setLayoutManager(layoutManager4);
        pred_app_from_server.setAdapter(predServerAdapter);
        initWebView();
    }
    public void initWebView(){
        webView=getActivity().findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);//允许与js 交互
        webView.getSettings().setDefaultTextEncodingName("utf-8");//支持中文
        //webView.addJavascriptInterface(new JsInterface(this), "androidYZH");//在js中调用本地java方法（androidYZH这个是js和安卓之间的约定，js：window.androidYZH.closeH5）
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                    return true;
                }
                return false;
            }

        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();

        //上传当前app名字到服务器，并获得推荐
        List<OneUse> allOneUses = appDao.getAllOneUses();
        DataSender.sendOneUses(allOneUses);
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
        if(allOneUses!=null){

            new Thread(()->{
                String ret = DataSender.sendOneUses(allOneUses);
                if(ret!=null) {
                    Bundle data = new Bundle();
                    data.putString("value", ret);
                    Message message = new Message();
                    message.setData(data);
                    handler.sendMessage(message);
                }
            }).start();
        }
        webView.loadUrl(DataSender.getUrl());



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){


        }
    }
}
