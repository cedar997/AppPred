package com.rom471.db2;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Query;

import com.rom471.db.AppBean;
import com.rom471.db.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppRecordsRepository {
    private Record record;
    AppDataBase appDataBase;
    AppDao appDao;
    static App getApp;
    LiveData<List<Record>> allRecords;
    public AppRecordsRepository(Application application){
        appDataBase=AppDataBase.getInstance(application);
        appDao=appDataBase.getAppDao();
        //List<App> allApps = appDao.getAllApps();

    }

    public LiveData<List<Record>> getAllRecords() {
        return allRecords;
    }

    public LiveData<List<App>>  getLastApp(int limit){
         return appDao.getLastUsedApp(limit);
    }
    public LiveData<List<App>>  getMostUsedApps(int limit){
        return appDao.getMostUsedApps(limit);
    }

    public void insert (OneUse oneUse) {
        new AppRecordsRepository.insertOneUseAsyncTask(appDao).execute(oneUse);
    }
    public void insert (App app) {
        new AppRecordsRepository.insertAsyncTask(appDao).execute(app);
    }
    public void update (App app) {
        new AppRecordsRepository.UpdateAsyncTask(appDao).execute(app);
    }
    public LiveData<List<OneUse>> getAllOneUses(){
        return appDao.getAllOneUse();
    }
    public void delete(OneUse oneUse){
        appDao.delete(oneUse);
    }
//    private static class getAsyncTask extends AsyncTask<String, Void, Void> {
//
//        private AppDao getAsyncTaskDao;
//        private App retApp;
//        getAsyncTask(AppDao dao) {
//            getAsyncTaskDao = dao;
//        }
//
//        @Override
//        protected Void doInBackground(final String... params) {
//            App[] app=null;
//            try{
//                app=getAsyncTaskDao.getAppByName(params[0]);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            if(app!=null&& app.length>0)
//                retApp=app[0];
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            getApp=retApp;
//        }
//    }
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
