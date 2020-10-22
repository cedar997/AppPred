package com.rom471.main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.rom471.lab1.R;
import com.rom471.userdb.MyUtils;
import com.rom471.userdb.User;
import com.rom471.userdb.UsersDBHelper;

import java.io.File;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    Button register_btn;
    EditText name_et;
    EditText password_et;
    EditText email_et;
    ImageView avatar_img;
    UsersDBHelper db;
    User user=new User();
    public final static int PICTURE=10;
    public final static int PHOTO=11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //绑定组件
        register_btn=findViewById(R.id.register_button);
        name_et=findViewById(R.id.name_et);
        password_et=findViewById(R.id.passwd_et);
        email_et=findViewById(R.id.email_et);
        avatar_img=findViewById(R.id.avatar_img);
        avatar_img.setOnClickListener(this);
        register_btn.setOnClickListener(this);
        db=new UsersDBHelper(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_button:
                String name=name_et.getText().toString();
                String password=password_et.getText().toString();
                String email=name_et.getText().toString();
                user.setName(name);
                user.setPassword(password);
                user.setEmail(email);

                db.insertUser(user);
                MyUtils.toast(this,"注册完成");
                db.close();
                //this.finish();
                break;
            case R.id.avatar_img:
                MyUtils.verifyStoragePermissions(this);
                getPhotoOrPicture();
                break;
        }
    }
    private void getPhotoOrPicture(){
        final CharSequence[] options = { "从相机", "从相册","取消" };
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("选择一个头像");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item==0)
                {
                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera, PHOTO);
                }
                else if (item==1)
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, PICTURE);
                }
                else if (item==2) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICTURE && resultCode==RESULT_OK && data!=null){
            String img_path=null;
            Uri uri=data.getData();

            img_path=uri.getPathSegments().get(1);
            MyUtils.log(img_path);
            Bitmap decodeFile= BitmapFactory.decodeFile(img_path);
            Bitmap zoomBitmap = MyUtils.zoomImg(decodeFile,200,200);
            Drawable avatar_drawable=new BitmapDrawable(zoomBitmap);
            avatar_img.setImageDrawable(avatar_drawable);
            user.setAvatar(avatar_drawable);
        }
        else if(requestCode==PHOTO && resultCode==RESULT_OK && data!=null){
            Bundle bundle = data.getExtras();
            // 获取相机返回的数据，并转换为图片格式
            Bitmap bitmap = (Bitmap) bundle.get("data");

            Bitmap zoomBitmap = MyUtils.zoomImg(bitmap,200,200);
            Drawable avatar_drawable=new BitmapDrawable(zoomBitmap);
            avatar_img.setImageDrawable(avatar_drawable);
            user.setAvatar(avatar_drawable);
        }
    }

}