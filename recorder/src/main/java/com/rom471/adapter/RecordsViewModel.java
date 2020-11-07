package com.rom471.adapter;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.rom471.db.Record;
import com.rom471.db.RecordDAO;
import com.rom471.db.RecordDataBase;

import java.util.List;

public class RecordsViewModel extends AndroidViewModel {
    LiveData<List<Record>> allRecords;

    private RecordsRepository recordsRepository;

    public RecordsViewModel(@NonNull Application application) {
        super(application);
        recordsRepository=new RecordsRepository(application);
        allRecords=recordsRepository.getAllRecords();
    }


//    public void setAllRecords(LiveData<List<Record>> allRecords) {
//        this.allRecords = allRecords;
//    }

    public LiveData<List<Record>> getAllRecords(){
        return allRecords;
    }
    public void insert(Record record){
        recordsRepository.insert(record);
    }
    public void delete(Record record){
        recordsRepository.delete(record);
    }
    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
