package com.rom471.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Record.class},version = 1,exportSchema = false)
public abstract class RecordDataBase extends RoomDatabase {
    public abstract RecordDAO getRecordDAO();

}
