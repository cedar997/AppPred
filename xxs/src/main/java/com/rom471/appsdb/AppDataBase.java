package com.rom471.appsdb;

import androidx.room.Database;
import androidx.room.RoomDatabase;
@Database(entities = {AppBean.class},version = 1,exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    public abstract AppDAO getAppDAO();
}
