package com.rom471.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.rom471.recorder.R;

import java.util.ArrayList;
import java.util.List;

public class RecordDBHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "apptime";

    private String db_name;
    public static final int DB_VERSION=1;

    //内部Context引用
    private Context context;
    //内部数据库引用
    private SQLiteDatabase db;

    public RecordDBHelper(Context context, String db_name){
        super(context,db_name,null,DB_VERSION);
        this.context=context;
        this.db_name=db_name;
        db=super.getWritableDatabase();
        createTable();

    }

    public void createTable(){
        String sql="create table if not exists "+TABLE_NAME+" ("
                + "id integer primary key autoincrement,"
                + "appname text ,"     //应用名
                + "battery integer,"    //电池电量
                + "charging integer,"    //功率
                + "net integer,"         //网络信息
                + "time integer DEFAULT  CURRENT_TIMESTAMP )";  //时间
        db.execSQL(sql);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void insertRecord(String app_name){
        ContentValues contentValues=new ContentValues();
        //contentValues.put("id",user.getId());
        contentValues.put("appname",app_name);

        db.insert(TABLE_NAME,null,contentValues);
    }
    public void insertRecord(Record record){
        long timecurrentTimeMillis = System.currentTimeMillis();
        record.setTimestamp(timecurrentTimeMillis);
        ContentValues contentValues=new ContentValues();
        //contentValues.put("id",user.getId());
        contentValues.put("appname",record.getAppname());
        contentValues.put("battery",record.getBattery());
        contentValues.put("charging",record.getCharging());
        contentValues.put("net",record.getNet());
        contentValues.put("time",record.getTimestamp());
        db.insert(TABLE_NAME,null,contentValues);
    }
    public void clearRecords(){
        String drop_sql="drop table "+TABLE_NAME;
        db.execSQL(drop_sql);
        createTable();

    }
    public List<Record> queryAll(){

        return queryAll("");
    }
    public List<Record> queryAll(String name) {
        List<Record> records=new ArrayList<>();
        String sql = "SELECT id,appname,time,battery,charging,net FROM "+TABLE_NAME +" order by id desc " ;
        Cursor cursor;
        if(name.equals(""))
            cursor=db.rawQuery(sql,null);
        else
            cursor = db.query(TABLE_NAME, new String[]{"id,appname,time, battery, charging,net"}, "appname=?", new String[]{name}, null, null, "id desc");
        //Cursor

        while(cursor.moveToNext()){
            int id=cursor.getInt(cursor.getColumnIndex("id"));
            String appname=cursor.getString(cursor.getColumnIndex("appname"));
            long timestamp=cursor.getLong(cursor.getColumnIndex("time"));
            int battery=cursor.getInt(cursor.getColumnIndex("battery"));
            int charging=cursor.getInt(cursor.getColumnIndex("charging"));
            int net=cursor.getInt(cursor.getColumnIndex("net"));
            Record record=new Record();
            record.setId(id);
            record.setAppname(appname);
            record.setTimestamp(timestamp);
            record.setBattery(battery);
            record.setCharging(charging);
            record.setNet(net);
            records.add(record);
        }
        cursor.close();
        return records;
    }
}
