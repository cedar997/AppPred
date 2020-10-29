package com.rom471.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;

import com.rom471.app.AppBean;

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
                +" pkgname text ," //包名
                + "battery integer,"    //电池电量
                + "timespend integer,"    //使用时间
                + "net integer,"         //网络信息
                + "charging integer,"
                + "time integer DEFAULT  CURRENT_TIMESTAMP )";  //开始时间
        db.execSQL(sql);
    }
    public List<Record> query1(){

        String sql="select appname,count(*) from apptime";
        Cursor cursor=db.rawQuery(sql,null);
        List<Record> records=new ArrayList<>();
        while(cursor.moveToNext()){

            String appname=cursor.getString(cursor.getColumnIndex("appname"));

            int count=cursor.getInt(cursor.getColumnIndex("count(*)"));
            Record record=new Record();

            records.add(record);
        }
        cursor.close();
        return records;

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
        record.setTimeStamp(timecurrentTimeMillis);
        ContentValues contentValues=new ContentValues();
        //contentValues.put("id",user.getId());
        contentValues.put("appname",record.getAppname());
        contentValues.put("pkgname",record.getPkgname());
        contentValues.put("battery",record.getBattery());
        contentValues.put("timespend",record.getTimeSpend());
        contentValues.put("net",record.getNet());
        contentValues.put("charging",record.getCharging());

        contentValues.put("time",record.getTimeStamp());
        db.insert(TABLE_NAME,null,contentValues);
    }
    public void clearRecords(){
        String drop_sql="drop table "+TABLE_NAME;
        db.execSQL(drop_sql);
        createTable();

    }

    public List<Record> queryLast(int limit) {
        String sql = "SELECT id,appname,pkgname,time,battery,charging,timespend,net FROM "+TABLE_NAME +" order by id desc limit "+limit ;
        Cursor cursor=db.rawQuery(sql,null);
        return readRecordsFromCursor(cursor);
    }
    public List<Record> queryAll() {
        String sql = "SELECT id,appname,pkgname,time,battery,charging,timespend,net FROM "+TABLE_NAME +" order by id desc " ;
        Cursor cursor=db.rawQuery(sql,null);
        return readRecordsFromCursor(cursor);
    }
    public List<Record> queryAllByName(String name){

       Cursor cursor = db.query(TABLE_NAME, new String[]{"id,appname,pkgname,time, battery,charging, timespend,net"},
                "appname=?", new String[]{name},
                null, null,
                "id desc");
       return readRecordsFromCursor(cursor);
    }
    private List<Record> readRecordsFromCursor(Cursor cursor){
        List<Record> records=new ArrayList<>();
        while(cursor.moveToNext()){
            int id=cursor.getInt(cursor.getColumnIndex("id"));
            String appname=cursor.getString(cursor.getColumnIndex("appname"));
            String pkgname=cursor.getString(cursor.getColumnIndex("pkgname"));
            long timestamp=cursor.getLong(cursor.getColumnIndex("time"));
            int battery=cursor.getInt(cursor.getColumnIndex("battery"));
            int charging=cursor.getInt(cursor.getColumnIndex("charging"));
            int timespend=cursor.getInt(cursor.getColumnIndex("timespend"));
            int net=cursor.getInt(cursor.getColumnIndex("net"));
            Record record=new Record();
            record.setId(id);
            record.setAppname(appname);
            record.setPkgname(pkgname);
            record.setTimeStamp(timestamp);
            record.setBattery(battery);
            record.setTimeSpend(timespend);
            record.setNet(net);
            record.setCharging(charging);
            records.add(record);
        }
        cursor.close();
        return records;
    }
    public List<AppBean> getLastAppBean(int limit){

        String sql = "SELECT id,appname,pkgname,time,timespend from "+TABLE_NAME +" order by id desc limit "+limit ;
        Cursor cursor=db.rawQuery(sql,null);
        return readAppBeanFromCursor(cursor);
    }
    public List<AppBean> getAppTotalTime(int limit){
        String fun="sum(timespend)";
        String sql = "SELECT appname,pkgname,"+fun+" from "+TABLE_NAME +" group by appname order by "+fun+"desc limit "+limit ;
//        String sql = "SELECT id,appname,pkgname,time,timespend from "+TABLE_NAME +" order by id  limit "+limit ;
        Cursor cursor=db.rawQuery(sql,null);
        List<AppBean> apps=new ArrayList<>();
        while(cursor.moveToNext()){
            //int id=cursor.getInt(cursor.getColumnIndex("id"));
            String appname=cursor.getString(cursor.getColumnIndex("appname"));
            String pkgname=cursor.getString(cursor.getColumnIndex("pkgname"));
            long timespend=cursor.getLong(cursor.getColumnIndex(fun));
            AppBean app=new AppBean();
            app.setAppname(appname);
            app.setPkgname(pkgname);
            app.setIcon(getIcon(pkgname));
            app.setTimeSpend(timespend);
            apps.add(app);
        }
        cursor.close();
        return apps;
    }
    private List<AppBean> readAppBeanFromCursor(Cursor cursor){
        List<AppBean> apps=new ArrayList<>();
        while(cursor.moveToNext()){
            int id=cursor.getInt(cursor.getColumnIndex("id"));
            String appname=cursor.getString(cursor.getColumnIndex("appname"));
            String pkgname=cursor.getString(cursor.getColumnIndex("pkgname"));
            long timespend=cursor.getLong(cursor.getColumnIndex("timespend"));
            AppBean app=new AppBean();
            app.setAppname(appname);
            app.setTimeSpend(timespend);
            app.setPkgname(pkgname);
            app.setIcon(getIcon(pkgname));
            apps.add(app);
        }
        cursor.close();
        return apps;
    }
    private Drawable getIcon(String pkgname) {
        PackageManager pm =context.getPackageManager();
        ApplicationInfo appInfo;
        Drawable appIcon;
        try {
            appInfo = pm.getApplicationInfo(pkgname, PackageManager.GET_META_DATA);

            appIcon = pm.getApplicationIcon(appInfo);
            return appIcon;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}
