package com.rom471.appsdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AppDAO {
    @Insert
    void insertApp(AppBean...appBeans);

    @Update
    void updateApp(AppBean...appBeans);
    @Delete
    void deleteApp(AppBean...appBeans);

    @Query("delete from AppBean")
    void deleteALl();
    @Query("select * from AppBean order by id desc")
    List<AppBean> getAllApps();
}
