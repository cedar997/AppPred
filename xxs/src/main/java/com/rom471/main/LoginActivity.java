package com.rom471.main;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rom471.lab1.R;
import com.rom471.userdb.UsersDBHelper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText name_et; //用户名输入框
    EditText password_et;//用户密码输入框
    UsersDBHelper db; //用户数据库操作类
    ImageView avatar_img; //用户头像图片
    TextView register_tv; //注册按钮
    TextView reset_tv;  //重设密码按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        //绑定资源
        name_et=findViewById(R.id.Acount);
        avatar_img=findViewById(R.id.pic);
        password_et=findViewById(R.id.passwd);
        Button login_bt=findViewById(R.id.loginbutton);
        register_tv=findViewById(R.id.register_tv);
        reset_tv=findViewById(R.id.forget);

        //设置按钮的点击事件监听器
        login_bt.setOnClickListener(this);
        register_tv.setOnClickListener(this);
        reset_tv.setOnClickListener(this);
        //根据名字输入框动态的查找用户的头像
        name_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name=s.toString();//获取用户输入的用户名
                if(!name.equals("")){
                    Drawable avatar_drawable = db.getAvatarByName(name);
                    if(avatar_drawable==null){
                        avatar_img.setImageResource(R.drawable.boy);
                    }
                    else {
                        avatar_img.setImageDrawable(avatar_drawable);
                    }
                }
                else {
                    avatar_img.setImageResource(R.drawable.boy);
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        //初始化数据库
        db=new UsersDBHelper(this);
        db.InitialWithTestData();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginbutton:
                //执行登录操作
                login_action();
                break;
            case R.id.register_tv:
                //跳转到注册界面
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class );
                startActivity(intent);
                break;
            case R.id.forget:
                //跳转到重设密码界面
                Intent intent2 = new Intent(LoginActivity.this, ResetPasswordActivity.class );
                startActivity(intent2);
                break;
        }
    }
    //登录操作
    private void login_action(){
        String name= name_et.getText().toString();
        String password=password_et.getText().toString();
        //先判断数据库里是否存在该用户
        if(!db.haveUser(name)){
            toast("该用户不存在！");
            return;
        }
        if(db.loginWith(name,password)){
            db.close();
            // 登录成功，跳转到Home界面
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class );
            intent.putExtra("name",name);
            startActivity(intent);
            this.finish();
        }
        else {
            toast("密码错误");
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        db.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        db=new UsersDBHelper(this);
    }
    //---------toast
    public  void toast( String text){
        Toast toast=Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}