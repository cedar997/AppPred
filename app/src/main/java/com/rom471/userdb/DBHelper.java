package com.rom471.userdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
                + "id integer primary key autoincrement,"
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
        //添加3个测试用户
        addUserWithAvatar("xxs","xxs",R.drawable.avatar_xxs);
        addUserWithAvatar("wjf","wjf",R.drawable.avatar_wjf);
        addUserWithAvatar("wkq","wkq",R.drawable.avatar_wkq);
    }
    //添加带头像的用户
    private void addUserWithAvatar(String name,String password,int rid){
        Drawable avatar=MyUtils.getDrawableFromResource(context,rid);
        User user=new User(name,password,avatar);
        this.insertUsers(user);
    }
    //向表中插入一个用户
    public void insertUsers(User user){
            ContentValues contentValues=new ContentValues();
            contentValues.put("name",user.getName());
            contentValues.put("password",user.getPassword());
            Drawable avatar = user.getAvatar();
            contentValues.put("avatar",MyUtils.getBytesFromDrawable(avatar));
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
            Drawable avatar=MyUtils.getDrawableFormCursor(cursor,"avatar");//直接从数据库获取头像Drawable
            User user=new User(name,password,avatar);
            user.setId(id);
            users.add(user);
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
