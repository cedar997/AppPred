package com.rom471.db2;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;


@Database(entities = {App.class,OneUse.class,Event.class},version = 1,exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    private static AppDataBase INSTANCE;//单例模式
    private static final Object Lock = new Object();
    public abstract AppDao appDao();
    public abstract OneUseDao oneUseDao();

    public AppDao getAppDao(){
        return INSTANCE.appDao();
    }
    public OneUseDao getOneUseDao(){
        return INSTANCE.oneUseDao();
    }
    public static AppDataBase getInstance(Context context){
        synchronized (Lock){
            if(INSTANCE==null){
                INSTANCE =
                        Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, "apps.db")
                                .allowMainThreadQueries()
                                .build();
            }
            return INSTANCE;
        }
    }
    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}
