package com.rom471.pred;

import android.app.Application;
import android.content.Context;
import android.widget.ListView;

import com.rom471.adapter.PredAdapter;
import com.rom471.db2.App;
import com.rom471.db2.AppDataBase;
import com.rom471.db2.OneUseDao;
import com.rom471.db2.SimpleApp;
import com.rom471.utils.DBUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.media.CamcorderProfile.get;

public class MyPredicter {
    SimpleApp top5[];
    SimpleApp near1[];
    SimpleApp near2[];
    List<SimpleApp> allUseCountsTop5;
    SimpleApp currentApp;

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
        currentApp= simpleDao.getCurrentApp();
    }
    private void getAppIndex(){
        app_index=simpleDao.getAllAppName();
        pkg_index=simpleDao.getAllPkgName();
        mSize=app_index.size();


    }
    //填充矩阵
    public void fillMatrix(){
        table=new int[app_index.size()][app_index.size()];//矩阵
        table2=new int[app_index.size()][app_index.size()];//矩阵
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
    public void updateAdapter(PredAdapter predAdapter){
        currentApp= simpleDao.getCurrentApp();
        getAppIndex();
        fillMatrix();


        List<SimpleApp> oneNearApps = getOneNear(currentApp);
        List<SimpleApp> twoNear = getTwoNear(currentApp);
        List<SimpleApp> most_apps = getMostCountsApps();


        List<SimpleApp> top_apps =merge(oneNearApps,twoNear);
        top_apps =merge(top_apps,most_apps);




        top_apps.sort(SimpleApp.weightDescComparator);
        DBUtils.setSimpleAppsIcon(context,top_apps );
        predAdapter.setmAppsList(top_apps);
        predAdapter.notifyDataSetChanged();
    }

}
