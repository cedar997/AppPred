package com.rom471.userdb;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import java.io.ByteArrayOutputStream;
public class User {
    private int id;
    private String name;
    private String password;
    private String email;
    private Drawable avatar;
    public void setAvatar(Drawable avatar) {
        this.avatar = avatar;
    }
    public User() {
        this.name = null;
        this.password = null;
        this.email = null;
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", avatarUrl='" + email + '\'' +
                '}';
    }

    public User( String name, String password, String email) {

        this.name = name;
        this.password = password;
        this.email = email;
    }
    //指定资源id，来设置用户的头像
    public void setAvatarResource(Context context, int id){
        Resources resources=context.getResources();
        Bitmap bmp= BitmapFactory.decodeResource(resources, id);
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG,100,baos);//压缩图片
        byte[] bytes = baos.toByteArray();
        this.avatar = byteToDrawable(bytes);
    }
    public Drawable getAvatar() {
        return avatar;
    }
    public byte[] getAvatarBytes(){
        return MyUtils.drawableToByteArray(this.avatar);
    }
    public static Drawable byteToDrawable(byte[] data) {
        if (data == null)
            return null;
        else
            return new BitmapDrawable(BitmapFactory.decodeByteArray(data, 0, data.length));
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
