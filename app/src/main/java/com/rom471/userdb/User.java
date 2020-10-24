package com.rom471.userdb;
import android.graphics.drawable.Drawable;
//用户的JavaBean
public class User {
    private int id;//用户id，由数据库指定
    private String name;//用户姓名
    private String password;//用户密码
    private Drawable avatar;//用户头像
    public User() {
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
    public User(String name, String password, Drawable avatar) {
        this.name = name;
        this.password = password;
        this.avatar = avatar;
    }
    public void setAvatar(Drawable avatar) {
        this.avatar = avatar;
    }
    public Drawable getAvatar() {
        return avatar;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setName(String name) {
        this.name = name;
    }


}
