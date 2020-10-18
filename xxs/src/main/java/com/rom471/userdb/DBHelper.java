package com.rom471.userdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

import com.rom471.lab1.R;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    //数据库名字
    public static final String DB_NAME="users.db";
    //存放用户信息的表名
    public static final String TABLE_NAME="User";
    public static final int DB_VERSION=1;
    //内部Context引用
    private Context context;
    //内部数据库引用
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
    //初始化内部引用
    public DBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
        db=super.getWritableDatabase();
        this.context=context;
    }
    //创建用户信息表
    public void createUserTable(){
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
    //判断是否存在表
    public  boolean havingTable(String table_name){
        Cursor cursor;
        boolean a=false;
        cursor = db.rawQuery("select name from sqlite_master where type='table' ", null);
        while(cursor.moveToNext()) {
            //遍历出表名
            String name = cursor.getString(0);
            if (name.equals(table_name)) { //存在表
                cursor = db.query(table_name, null, null, null, null, null, null);
                //检查是不是空表
                if (cursor.getCount() > 0) {
                    return true;
                } else //空表
                    return false;
            }
        }
        return false;
    }

    //用测试数据初始化数据库
    public  void InitialWithTestData(){
        if(havingTable(TABLE_NAME))
            return;
        createUserTable();

        addUserWithAvatar(1,"谢雪松","cedar","527474091@qq.com",R.drawable.avatar1);
        addUserWithAvatar(2,"wjf","wjf","wjf@qq.com",R.drawable.avatar2);
        addUserWithAvatar(3,"wkq","wkq",null,R.drawable.avatar3);
        addUserWithAvatar(4,"马云","0",null,R.drawable.mayun);
    }
    //添加带头像的用户
    private void addUserWithAvatar(int id,String name,String password,String email,int rid){
        User user=new User(id,name,password,email);
        user.setAvatarResource(context,rid);
        this.insertUsers(user);
    }

    //向表中插入一个用户
    public void insertUsers(User user){
            ContentValues contentValues=new ContentValues();
            contentValues.put("id",user.getId());
            contentValues.put("name",user.getName());
            contentValues.put("password",user.getPassword());
            contentValues.put("avatar",user.getAvatarBytes());
            db.insert(TABLE_NAME,null,contentValues);
    }
    //查询所有用户并返回
    public List<User> queryAll(){
        List<User> users=new ArrayList<>();
        Cursor cursor=db.query(TABLE_NAME,new String[]{"id","name","password","avatar"},null,null,null,null,null);
        while(cursor.moveToNext()){
            int id=cursor.getInt(cursor.getColumnIndex("id"));
            String name=cursor.getString(cursor.getColumnIndex("name"));
            String password=cursor.getString(cursor.getColumnIndex("password"));
            String email=cursor.getString(cursor.getColumnIndex("email"));
            users.add(new User(id,name,password,email));

        }
        cursor.close();
        return users;
    }
    //判断用户名是否存在
    public boolean haveUser(String name){
        String sql=" select * from "+TABLE_NAME+ " where name = ?";
        Cursor cursor=db.rawQuery(sql,new String[]{name});
        if(cursor.moveToFirst()==true){
            cursor.close();
            return true;
        }
        return false;
    }
    //使用用户名和密码登录
    public boolean loginWith(String name,String password){
        String sql=" select * from "+TABLE_NAME+ " where name = ? and password=?";
        Cursor cursor=db.rawQuery(sql,new String[]{name,password});
        if(cursor.moveToFirst()==true){
            cursor.close();
            return true;
        }
        return false;
    }
    //由用户名获取头像
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
