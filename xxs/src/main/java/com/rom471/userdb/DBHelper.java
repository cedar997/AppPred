package com.rom471.userdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public static final String CREATE_USER="create table User ("
            + "id integer primary key,"
            + "name text ,"
            + "password text,"
            + "avatar blob)";

    public static final String DB_NAME="users.db";
    public static final String TABLE_NAME="User";
    public static final int DB_VERSION=1;

    private SQLiteDatabase db;
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(TABLE_NAME);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public DBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
        this.db=this.getWritableDatabase();
    }
    public void createTable(String table_name){
        db.execSQL(CREATE_USER);
    }
    public void dropTable(String table_name){

        db.execSQL("drop table "+table_name);
    }
    public  void InitialWithTestData(){
        dropTable(TABLE_NAME);
        createTable(TABLE_NAME);
        User u1=new User(1,"xxs","cedar",null);
        User u2=new User(2,"wjf","wjf",null);
        this.insert(u1);
        this.insert(u2);
    }

    public void insert(User... users){
        for (User user:users
             ) {
            ContentValues contentValues=new ContentValues();
            contentValues.put("id",user.getId());
            contentValues.put("name",user.getName());
            contentValues.put("password",user.getPassword());
            contentValues.put("avatar",user.getAvatarUrl());
            db.insert(TABLE_NAME,null,contentValues);
        }

    }
    public List<User> querryAll(){
        List<User> users=new ArrayList<>();
        Cursor cursor=db.query(TABLE_NAME,new String[]{"id","name","password","avatar"},null,null,null,null,null);
        while(cursor.moveToNext()){
            int id=cursor.getInt(cursor.getColumnIndex("id"));
            String name=cursor.getString(cursor.getColumnIndex("name"));
            String password=cursor.getString(cursor.getColumnIndex("password"));
            String avatar=cursor.getString(cursor.getColumnIndex("avatar"));
            users.add(new User(id,name,password,avatar));

        }
        cursor.close();
        return users;
    }
    public boolean haveUser(String name){
        String sql=" select * from "+TABLE_NAME+ " where name = ?";
        Cursor cursor=db.rawQuery(sql,new String[]{name});
        if(cursor.moveToFirst()==true){
            cursor.close();
            return true;
        }
        return false;
    }
    public boolean loginWith(String name,String password){
        String sql=" select * from "+TABLE_NAME+ " where name = ? and password=?";
        Cursor cursor=db.rawQuery(sql,new String[]{name,password});
        if(cursor.moveToFirst()==true){
            cursor.close();
            return true;
        }
        return false;
    }


}
