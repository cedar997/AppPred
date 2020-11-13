package com.rom471.pred;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.rom471.adapter.PredAdapter;
import com.rom471.db2.AppDataBase;
import com.rom471.db2.OnePred;
import com.rom471.db2.OneUseDao;
import com.rom471.db2.SimpleApp;
import com.rom471.net.DataSender;
import com.rom471.utils.DBUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.media.CamcorderProfile.get;

public class MyPredicter {
    //private static final String TAG = "cedar";
    SimpleApp top5[];
    SimpleApp near1[];
    SimpleApp near2[];
    List<SimpleApp> allUseCountsTop5;
    SimpleApp currentApp;
    SimpleApp lastApp;

    OneUseDao simpleDao;
    Context context;
    List<String> app_index;
    List<String> pkg_index;
    int mSize;
    int[][] table;
    int[][] table2;
    int one_rate=3;
    int two_rate=2;
    int most_rate=2;
    public MyPredicter(Application context){
        this.context=context;


        AppDataBase appDataBase= AppDataBase.getInstance(context);

        simpleDao =appDataBase.getOneUseDao();
        allUseCountsTop5 = simpleDao.getMostCountsApps(5);
        initAppIndex();
        initMatrix();
    }
    private void initAppIndex(){
        app_index=simpleDao.getAllAppName();
        pkg_index=simpleDao.getAllPkgName();
        mSize=app_index.size();
        mSize=mSize+mSize/2;//1.5倍
    }
    //更新矩阵
    private void updateMatrix(SimpleApp lastApp){

        List<SimpleApp> all = simpleDao.getAfter(lastApp.getStartTimestamp());
        for(SimpleApp app:all){
           if(!app_index.contains(app.getAppName())){
               app_index.add(app.getAppName());
               pkg_index.add(app.getPkgName());
               if(app_index.size()>mSize){//如果超出了预分配范围
                   initMatrix();//重新建立矩阵
                   return;
               }
           }
        }
        for(int i = 0; i< all.size()-1 ; i++){

            int index1=app_index.indexOf(lastApp.getAppName());
            int index2=app_index.indexOf(all.get(i).getAppName());
            int index3=app_index.indexOf(all.get(i+1).getAppName());
            if(index1!=index2)
                table[index1][index2]+=1;
            if(index1!=index3)
                table2[index1][index3]+=1;
        }
    }
    //建立应用打开矩阵
    public void initMatrix(){
        table=new int[mSize][mSize];//矩阵
        table2=new int[mSize][mSize];//矩阵
        List<SimpleApp> all = simpleDao.getAll();
        for(int i = 0; i< all.size()-2 ; i++){
            int index1=app_index.indexOf(all.get(i).getAppName());
            int index2=app_index.indexOf(all.get(i+1).getAppName());
            int index3=app_index.indexOf(all.get(i+2).getAppName());
            if(index1!=index2)
                table[index1][index2]+=1;
            if(index1!=index3)
                table2[index1][index3]+=1;
        }
    }




    public List<SimpleApp> getOneNear(SimpleApp currentApp){

        int current_index=app_index.indexOf(currentApp.getAppName());
        List<SimpleApp> ret=new ArrayList<>();
        for(int i=0;i<app_index.size();i++){
            if(table[current_index][i]!=0){
                SimpleApp app=new SimpleApp();
                app.setAppName(app_index.get(i));
                app.setPkgName(pkg_index.get(i));
                app.setWeight(table[current_index][i]*one_rate);
                ret.add(app);
            }
        }
        return ret;
    }

    public List<SimpleApp> getTwoNear(SimpleApp currentApp){

        int current_index=app_index.indexOf(currentApp.getAppName());
        List<SimpleApp> ret=new ArrayList<>();
        for(int i=0;i<app_index.size();i++){
            if(table2[current_index][i]!=0){
                SimpleApp app=new SimpleApp();
                app.setAppName(app_index.get(i));
                app.setPkgName(pkg_index.get(i));
                app.setWeight(table2[current_index][i]*two_rate);
                ret.add(app);
            }
        }
        
        return ret;
    }


    private List<SimpleApp> getMostCountsApps(){
        List<SimpleApp> most_apps = simpleDao.getMostCountsApps(5);
        int rate=most_rate;
        for(SimpleApp app:most_apps){
            app.setWeight(rate);
            rate/=2;
        }
        return most_apps;
    }

    public List<SimpleApp> merge(final List<SimpleApp> old_list, final List<SimpleApp> new_list){
        final Map<String, SimpleApp> listMap = new LinkedHashMap<>();
        for(SimpleApp app:old_list){
            String appname=app.getAppName();
            if(listMap.containsKey(appname)){
                SimpleApp app1 = listMap.get(appname);
                app.addWeight(app1.getWeight());
            }
            listMap.put(app.getAppName(),app);
        }
        for(SimpleApp app:new_list){
            String appname=app.getAppName();
            if(listMap.containsKey(appname)){
                SimpleApp app1 = listMap.get(appname);
                app.addWeight(app1.getWeight());
            }
            listMap.put(appname,app);
        }
        return new ArrayList<>(listMap.values());
    }
    public OnePred getOnePred(){
        if(currentApp==null)
            return null;
        SimpleApp pred= simpleDao.getCurrentApp();
        //Log.d(TAG, "updateAdapter: "+currentApp.getAppName());


        List<SimpleApp> oneNearApps = getOneNear(currentApp);
        List<SimpleApp> twoNear = getTwoNear(currentApp);
        List<SimpleApp> most_apps = getMostCountsApps();
        List<SimpleApp> top_apps =merge(oneNearApps,twoNear);
        top_apps =merge(top_apps,most_apps);
        top_apps.sort(SimpleApp.weightDescComparator);
        if(top_apps.size()>5)
            top_apps=top_apps.subList(0,5);
        int pred_index = top_apps.indexOf(pred);
        OnePred onePred=new OnePred();
        if(pred_index==1){
            onePred.setTop1(1);
        }else if(pred_index<3){
            onePred.setTop3(1);
        }
        else if(pred_index<5){
            onePred.setTop5(1);
        }
        return onePred;
    }
    public void    updateAdapter(PredAdapter predAdapter){
        currentApp= simpleDao.getCurrentApp();

        if(currentApp==null)
            return;
        //Log.d(TAG, "updateAdapter: "+currentApp.getAppName());
        if(lastApp==null){ //如果第一次预测

            initAppIndex();
            initMatrix();
            lastApp=currentApp;
        }
        else {

            updateMatrix(lastApp);
        }

        List<SimpleApp> oneNearApps = getOneNear(currentApp);
        List<SimpleApp> twoNear = getTwoNear(currentApp);
        List<SimpleApp> most_apps = getMostCountsApps();
        List<SimpleApp> top_apps =merge(oneNearApps,twoNear);
        top_apps =merge(top_apps,most_apps);
        top_apps.sort(SimpleApp.weightDescComparator);
        if(top_apps.size()>5)
            top_apps=top_apps.subList(0,5);
        DBUtils.setSimpleAppsIcon(context,top_apps );
        predAdapter.setmAppsList(top_apps);
        predAdapter.notifyDataSetChanged();
    }

}
