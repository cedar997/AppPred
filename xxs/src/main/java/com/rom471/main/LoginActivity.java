package com.rom471.main;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rom471.lab1.R;
import com.rom471.userdb.MyUtils;
import com.rom471.userdb.UsersDBHelper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    EditText name_et;
    EditText password_et;
    UsersDBHelper db;
    ImageView avatar_img;
    TextView register_tv;
    TextView reset_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        avatar_img=findViewById(R.id.pic);
        Button login_bt=findViewById(R.id.loginbutton);
        login_bt.setOnClickListener(this);
        name_et=findViewById(R.id.Acount);
        password_et=findViewById(R.id.passwd);
        name_et.setOnFocusChangeListener(this);
        password_et.setOnFocusChangeListener(this);
        register_tv=findViewById(R.id.register_tv);
        register_tv.setOnClickListener(this);
        reset_tv=findViewById(R.id.forget);
        reset_tv.setOnClickListener(this);
        db=new UsersDBHelper(this);
        db.InitialWithTestData();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginbutton:

                login_action();
                break;
            case R.id.register_tv:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class );
                startActivity(intent);
                break;
            case R.id.forget:
                Intent intent2 = new Intent(LoginActivity.this, ResetPasswordActivity.class );
                startActivity(intent2);
                break;
        }
    }
    private void login_action(){
        String name= name_et.getText().toString();
        String password=password_et.getText().toString();
        if(!db.haveUser(name)){
            MyUtils.toast(this,"该用户不存在！");
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
            MyUtils.toast(this,"密码错误");
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.Acount:
                if (hasFocus)
                    ((EditText)v).setHint("");
                else {
                    ((EditText) v).setHint(R.string.account);
                    String name=name_et.getText().toString();

                    if(!name.equals("")){
                        Drawable avatar_drawable = db.getAvatarByName(name);
                        if(avatar_drawable==null){
                            avatar_img.setImageResource(R.drawable.head);
                        }
                        else {
                            avatar_img.setImageDrawable(avatar_drawable);
                        }
                    }
                    else {
                        avatar_img.setImageResource(R.drawable.head);
                    }
                }
                break;
            case R.id.passwd:

                if (hasFocus) {

                    ((EditText) v).setHint("");
                }
                else
                    ((EditText)v).setHint(R.string.passwd);
                break;
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
}