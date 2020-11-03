package com.rom471.room;

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
    List<Record> getAllApps();
}
