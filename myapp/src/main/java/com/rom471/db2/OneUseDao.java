package com.rom471.db2;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public abstract class OneUseDao {
    @Query("select appName,pkgName,startTimestamp from OneUse ")
    public abstract List<SimpleApp> getAll();

    @Query("select appName,pkgName,startTimestamp from OneUse where startTimestamp>:startTimeStamp")
    public abstract List<SimpleApp> getAfter(long startTimeStamp);

    @Query("select appName,pkgName,startTimestamp  from OneUse order by id desc limit 1")
    public abstract SimpleApp getCurrentApp();

    @Query("select appName,pkgName,lastRuningTime startTimestamp  from App order by useCount desc  limit :limit")
    public abstract List<SimpleApp>  getMostCountsApps(int limit);
    @Query("select appName from App order by appName")
    public abstract List<String> getAllAppName();
    @Query("select pkgName from App order by appName")
    public abstract List<String> getAllPkgName();

    @Insert
    public abstract void insert(OnePred onePred);

    @Query("select count(*) from OnePred ")
    public abstract int getPredCounts();

    @Query("select count(*) from OnePred where top1=1")
    public abstract int getPredTop1Counts();
}
