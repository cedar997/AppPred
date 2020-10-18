package com.rom471.userdb;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

import com.rom471.lab1.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {


    public static final String DB_NAME="users1.db";
    public static final String TABLE_NAME="User";
    public static final int DB_VERSION=1;

    private SQLiteDatabase db;
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public DBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
        db=super.getWritableDatabase();
    }
    public void createTable(String table_name){
        final String CREATE_USER="create table User ("
                + "id integer primary key,"
                + "name text ,"
                + "password text,"
                + "avatar blob)";
        db.execSQL(CREATE_USER);
    }

    public void dropTable(String table_name){
        db.execSQL("drop table if exists "+table_name);
    }
    public  boolean havingTable(String tablename){
        Cursor cursor;
        boolean a=false;
        cursor = db.rawQuery("select name from sqlite_master where type='table' ", null);
        while(cursor.moveToNext()){
            //遍历出表名
            String name = cursor.getString(0);
            if(name.equals(tablename))
            {
                a=true;
            }

        }
        if(a)
        {
            cursor=db.query(tablename,null,null,null,null,null,null);
            //检查是不是空表
            if(cursor.getCount()>0)
                return true;
            else
                return false;
        }
        else
            return false;

    }

    //用测试数据初始化数据库
    public  void InitialWithTestData(Context context){
        if(havingTable(TABLE_NAME))
            return;
        createTable(TABLE_NAME);
        User u1=new User(1,"xxs","cedar",null);
        User u2=new User(2,"wjf","wjf",null);
        User u3=new User(3,"wkq","wkq",null);

        u1.setAvatarResource(context,R.drawable.avatar1);
        u2.setAvatarResource(context,R.drawable.avatar2);
        u3.setAvatarResource(context,R.drawable.avatar3);
        this.insertUsers(u1,u2,u3);
    }

    public void insertUsers(User... users){
        for (User user:users
             ) {
            ContentValues contentValues=new ContentValues();
            contentValues.put("id",user.getId());
            contentValues.put("name",user.getName());
            contentValues.put("password",user.getPassword());
            contentValues.put("avatar",user.getAvatarBytes());
            db.insert(TABLE_NAME,null,contentValues);
        }

    }
    public List<User> queryAll(){
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
    public void inserUser(int id,String name,String password,String avatar){
        ContentValues contentValues=new ContentValues();
        contentValues.put("id",id);
        contentValues.put("name",name);
        contentValues.put("password",password);

        contentValues.put("avatar",avatar);
        db.insert(TABLE_NAME,null,contentValues);
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
    public Drawable getAvatarByName(String name){
        String sql = "SELECT avatar FROM "+TABLE_NAME +" WHERE name = ?";
        Drawable avatar=null;
        byte[] bytes;
        Cursor cursor=db.rawQuery(sql,new String[]{name});
        if(cursor.moveToFirst()){
            if((bytes=cursor.getBlob(cursor.getColumnIndex("avatar")))!=null){
                avatar= new BitmapDrawable(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
        }

        cursor.close();
        return avatar;
    }


}
