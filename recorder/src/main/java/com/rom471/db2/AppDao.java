package com.rom471.db2;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;



import java.util.List;

@Dao
public abstract class AppDao {

    @Insert
    abstract void insertOneUse(OneUse...oneUses);
    @Insert
    abstract void insertAll(List<OneUse> records);
    @Query("select * from App order by lastRuningTime desc  limit :limit")
    public abstract LiveData<List<App>>  getLastUsedApp(int limit);
    @Query("select * from App order by totalRuningTime desc  limit :limit")
    public abstract LiveData<List<App>>  getMostUsedApps(int limit);
    @Update
    public abstract void updateApp(App app);
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insertApp(App app);

    @Query("select * from App where appName=:appname limit 1")
    public abstract App getAppByName(String appname);
    @Query("select * from OneUse order by id desc")
    public abstract LiveData<List<OneUse>>  getAllOneUse();
    @Insert
    public abstract void insert(App app);
    @Insert
    public abstract void insert(OneUse oneUse);
    @Insert
    public abstract void insert(Event app);
    @Query("select * from Event order by id desc")
    public abstract LiveData<List<Event>>  getAllEvents();
    @Delete
    public abstract void delete(OneUse oneUse);
    @Query("delete from App")
    public abstract void deleteApps();
    @Query("delete from OneUse")
    public abstract void deleteOneUses();
//    @Query("select appname,pkgname, timeSpend from Record order by id desc  limit :limit")
//    public List<AppBean> getLastApp(int limit);
//    @Query("select appname,pkgname, sum(timeSpend) timeSpend from Record group by appname order by sum(timeSpend) desc limit :limit")
//    public List<AppBean> getAppTotalTime(int limit);
}
