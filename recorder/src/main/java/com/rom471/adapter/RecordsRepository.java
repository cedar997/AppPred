package com.rom471.adapter;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.rom471.db.Record;
import com.rom471.db.RecordDAO;
import com.rom471.db.RecordDataBase;

import java.util.List;

public class RecordsRepository {
    private Record record;
    RecordDataBase recordDataBase;
    RecordDAO recordDAO;
    LiveData<List<Record>> allRecords;
    RecordsRepository(Application application){
        RecordDataBase recordDataBase=RecordDataBase.getDatabase(application);
        recordDAO=recordDataBase.getRecordDAO();
        allRecords= recordDAO.getRecordsLive(100);
    }

    public LiveData<List<Record>> getAllRecords() {
        return allRecords;
    }
    public void insert (Record word) {
        new insertAsyncTask(recordDAO).execute(word);
    }
    private static class insertAsyncTask extends AsyncTask<Record, Void, Void> {

        private RecordDAO mAsyncTaskDao;

        insertAsyncTask(RecordDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Record... params) {
            mAsyncTaskDao.insertRecord(params[0]);
            return null;
        }
    }
}
