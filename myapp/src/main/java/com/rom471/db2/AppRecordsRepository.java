package com.rom471.db2;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AppRecordsRepository {

    AppDataBase appDataBase;
    AppDao appDao;
    static App getApp;

    public AppRecordsRepository(Application application){
        appDataBase=AppDataBase.getInstance(application);
        appDao=appDataBase.getAppDao();
        //List<App> allApps = appDao.getAllApps();

    }



    public LiveData<List<App>>  getLastApp(int limit){
         return appDao.getLastUsedApp(limit);
    }
    public LiveData<List<App>>  getMostUsedApps(int limit){
        return appDao.getMostUsedApps(limit);
    }
    public LiveData<List<App>>  getMostCountsApps(int limit){
        return appDao.getMostCountsApps(limit);
    }

    public LiveData<List<OneUse>> getAllOneUsesLive(){
        return appDao.getAllOneUsesLive();
    }
    public List<OneUse> getAllOneUses(){
        return appDao.getAllOneUses();
    }
    public void delete(OneUse oneUse){
        appDao.delete(oneUse);
    }
    public void deleteAll(){
        appDao.deleteApps();
        appDao.deleteOneUses();
        appDao.deleteOnePreds();
    }
    public void deletePreds(){

        appDao.deleteOnePreds();
    }

}
