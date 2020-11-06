package com.rom471.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Record.class},version = 1,exportSchema = false)
public abstract class RecordDataBase extends RoomDatabase {
    public abstract RecordDAO getRecordDAO();
    private static RecordDataBase INSTANCE;
    public static RecordDataBase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RecordDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RecordDataBase.class, "records.db")
                            // Wipes and rebuilds instead of migrating 
                            // if no Migration object.
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
