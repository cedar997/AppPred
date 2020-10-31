package com.rom471.main;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.rom471.lab1.R;
import com.rom471.userdb.MyUtils;
import com.rom471.userdb.User;
import com.rom471.userdb.UsersDBHelper;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    Button register_btn; //注册按钮
    EditText name_et; //用户名输入框
    EditText password_et;//密码输入框
    EditText email_et; //用户邮箱输入框
    ImageView avatar_img; //用户头像图片
    UsersDBHelper db; //用户数据库操作类
    User User =new User(); //用户对象
    public final static int PICTURE=10; //通过相册获取用户头像的请求码
    public final static int CAMERA =11; //通过相机获取用户头像的请求码
    boolean userNameValid=true;  //记录输入的用户名是否合法
    boolean registerFinish=false; //注册是否完成
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
        //设置点击事件监听器
        avatar_img.setOnClickListener(this);
        register_btn.setOnClickListener(this);
        //设置用户名输入框的监视器
        name_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name=s.toString();//获取用户输入的用户名
                if(db.haveUser(name)){ //该用户名已经存在
                    name_et.setTextColor(Color.RED);
                    userNameValid=false;
                }
                else {
                    name_et.setTextColor(Color.BLACK);
                    userNameValid=true;
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        //给用户数据库操作类赋值
        db=new UsersDBHelper(this);

    }

    @Override
    public void onClick(View v) {
        String name=name_et.getText().toString();
        String password=password_et.getText().toString();
        String email=email_et.getText().toString();
        switch (v.getId()){
            case R.id.register_button:
                if(registerFinish){
                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class );
                    intent.putExtra("name",name);
                    startActivity(intent);
                    this.finish();
                }
                if(!userNameValid){
                    toast("该用户名已经被使用！");
                    return;
                }
                User.setName(name);
                User.setPassword(password);
                User.setEmail(email);
                db.insertUser(User);
                MyUtils.toast(this,"注册成功");
                db.close();
                registerFinish=true;
                hideViews();
                //this.finish();
                break;
            case R.id.avatar_img:
                MyUtils.verifyStoragePermissions(this);
                getPhotoOrPicture();
                break;
        }
    }
    //注册成功后删除一些view
    private void hideViews(){
        ViewGroup parent =(ViewGroup) name_et.getParent();
        parent.removeView(email_et);
        parent.removeView(password_et);
        name_et.setFocusable(false);
        register_btn.setText("登录");
    }
    //弹出对话框，选择从哪里获得头像
    private void getPhotoOrPicture(){
        final CharSequence[] options = { "从相机", "从相册","取消" };
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("选择一个头像");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item==0){ // 打开系统相机获取头像图片
                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera, CAMERA);
                }
                else if (item==1){  //打开系统相册获取头像图片
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, PICTURE);
                }
                else if (item==2) { //取消对话框
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
            User.setAvatar(avatar_drawable);
        }
        else if(requestCode== CAMERA && resultCode==RESULT_OK && data!=null){
            Bundle bundle = data.getExtras();
            // 获取相机返回的数据，并转换为图片格式
            Bitmap bitmap = (Bitmap) bundle.get("data");
            Bitmap zoomBitmap = MyUtils.zoomImg(bitmap,200,200);
            Drawable avatar_drawable=new BitmapDrawable(zoomBitmap);
            avatar_img.setImageDrawable(avatar_drawable);
            User.setAvatar(avatar_drawable);
        }
    }
    //---------toast
    public  void toast( String text){
        Toast toast=Toast.makeText(RegisterActivity.this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}