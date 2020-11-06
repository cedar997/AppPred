package com.rom471.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecordDAO {
    @Insert
    void insertRecord(Record...records);

    @Update
    void updateRecord(Record...records);
    @Delete
    void deleteRecord(Record...records);

    @Query("delete from Record")
    void deleteALl();
    @Query("select * from Record order by id desc")
    List<Record> getAllRecords();
    @Query("select * from Record order by id desc limit :limit")
    List<Record> getRecords(int limit);
    @Query("select * from Record order by id desc limit :limit")
    LiveData<List<Record>>  getRecordsLive(int limit);
    @Query("select appname,pkgname, timeSpend from Record order by id desc  limit :limit")
    public List<AppBean> getLastApp(int limit);
    @Query("select appname,pkgname, sum(timeSpend) timeSpend from Record group by appname order by sum(timeSpend) desc limit :limit")
    public List<AppBean> getAppTotalTime(int limit);

}
