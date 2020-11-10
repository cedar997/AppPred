package com.rom471.db2;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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

    public void insert (OneUse oneUse) {
        new insertOneUseAsyncTask(appDao).execute(oneUse);
    }
    public void insert (App app) {
        new insertAsyncTask(appDao).execute(app);
    }
    public void update (App app) {
        new UpdateAsyncTask(appDao).execute(app);
    }
    public LiveData<List<OneUse>> getAllOneUses(){
        return appDao.getAllOneUse();
    }
    public void delete(OneUse oneUse){
        appDao.delete(oneUse);
    }
    public void deleteAll(){
        appDao.deleteApps();
        appDao.deleteOneUses();
    }
    private static class insertAsyncTask extends AsyncTask<App, Void, Void> {

        private AppDao mAsyncTaskDao;

        insertAsyncTask(AppDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final App... params) {
            try{
                mAsyncTaskDao.insertApp(params[0]);
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
    }
    private static class UpdateAsyncTask extends AsyncTask<App, Void, Void> {

        private AppDao mAsyncTaskDao;

        UpdateAsyncTask(AppDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final App... params) {
            try{
                mAsyncTaskDao.updateApp(params[0]);
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
    }
    private static class insertOneUseAsyncTask extends AsyncTask<OneUse, Void, Void> {

        private AppDao mAsyncTaskDao;

        insertOneUseAsyncTask(AppDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final OneUse... params) {
            try{
                mAsyncTaskDao.insertOneUse(params[0]);
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
    }
}
