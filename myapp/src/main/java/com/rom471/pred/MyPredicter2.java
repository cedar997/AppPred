package com.rom471.pred;

import android.app.Application;
import android.content.Context;

import com.google.android.material.appbar.AppBarLayout;
import com.rom471.adapter.PredAdapter;
import com.rom471.db2.AppDataBase;
import com.rom471.db2.OnePred;
import com.rom471.db2.OneUseDao;
import com.rom471.db2.SimpleApp;
import com.rom471.utils.DBUtils;

import org.w3c.dom.ls.LSException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MyPredicter2 {
    static MyPredicter2 INSTANCE;
    public static MyPredicter2 getInstance(Application context){
        if(INSTANCE!=null)
            return INSTANCE;
        INSTANCE=new MyPredicter2(context);
        return INSTANCE;
    }
    //面向服务的预测器
    //private static final String TAG = "cedar";
    SimpleApp top5[];
    SimpleApp near1[];
    SimpleApp near2[];
    List<SimpleApp> allUseCountsTop5;
    SimpleApp currentApp;
    SimpleApp last1App;
    SimpleApp last2App;

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
    public MyPredicter2(Application context){
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
        mSize=mSize+20;//
    }
    private void updateIndex(SimpleApp lastApp){
        if(!app_index.contains(lastApp.getAppName())){
            app_index.add(lastApp.getAppName());
            pkg_index.add(lastApp.getPkgName());
            if(app_index.size()>mSize){//如果超出了预分配范围
                initMatrix();//重新建立矩阵
                return;
            }
        }
    }
    //更新矩阵
    public void updateMatrix(SimpleApp lastApp){
           updateIndex(lastApp);
            int index1=app_index.indexOf(last2App.getAppName());
            int index2=app_index.indexOf(last1App.getAppName());
            int index3=app_index.indexOf(lastApp.getAppName());
            if(index1!=index2)
                table[index1][index2]+=1;
            if(index1!=index3)
                table2[index1][index3]+=1;
            last2App=last1App;
            last1App=lastApp;

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
        if(all.size()>=2) {
            last1App = all.get(all.size() - 1);
            last2App = all.get(all.size() - 2);
            currentApp = last1App;
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
    public OnePred getOnePred(SimpleApp app){
        if(currentApp==null)
            return null;
        updateIndex(currentApp);
        List<SimpleApp> oneNearApps = getOneNear(currentApp);
        List<SimpleApp> twoNear = getTwoNear(currentApp);
        List<SimpleApp> most_apps = getMostCountsApps();
        List<SimpleApp> top_apps =merge(oneNearApps,twoNear);
        top_apps =merge(top_apps,most_apps);
        top_apps.sort(SimpleApp.weightDescComparator);
        if(top_apps.size()>5)
            top_apps=top_apps.subList(0,5);
        int pred_index = top_apps.indexOf(app);
        OnePred onePred=null;
        switch (pred_index){
            case 1:
                onePred=new OnePred(1,1,1);
                 break;
            case 2: case 3:
                onePred=new OnePred(0,1,1);
                break;
            case 4: case 5:
                onePred=new OnePred(0,0,1);
                break;
        }

        return onePred;
    }


}
