package com.rom471.db2;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;


@Database(entities = {App.class,OneUse.class,Event.class,OnePred.class},version = 2,exportSchema = false)
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
                                .addMigrations(MIGRATION_Add_)
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

    static Migration MIGRATION_Add_= new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

            //  添加新的表
            database.execSQL("CREATE TABLE IF NOT EXISTS OnePred" +
                    " ( id INTEGER PRIMARY KEY not null," +
                    "top1 INTEGER not null," +
                    "top3 INTEGER not null," +
                    "top5 INTEGER not null) ");

        }
    };
}
