package com.rom471.userdb;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
//工具类
public class MyUtils {

    //指定资源id，来设置用户的头像
    public static Drawable getDrawableFromResource(Context context, int rid){
        Resources resources=context.getResources();
        Bitmap bmp= BitmapFactory.decodeResource(resources, rid);
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG,100,baos);//压缩图片
        byte[] bytes = baos.toByteArray();
        return getDrawableFromBytes(bytes);
    }
    //将Drawable对象转换为bytes好存入数据库
    public static byte[] getBytesFromDrawable(Drawable d) {

        if (d != null) {
            Bitmap imageBitmap = ((BitmapDrawable) d).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] byteData = baos.toByteArray();

            return byteData;
        } else
            return null;

    }



    public static Drawable getDrawableFromBytes(byte[] data) {

        if (data == null)
            return null;
        else
            return new BitmapDrawable(BitmapFactory.decodeByteArray(data, 0, data.length));
    }
    public static Drawable getDrawableFormCursor(Cursor cursor,String index){
        byte[] avatar_bytes= cursor.getBlob(cursor.getColumnIndex("avatar"));
        return getDrawableFromBytes(avatar_bytes);
    }
}
