package com.rom471.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import com.rom471.lab1.R;
import com.rom471.userdb.DBHelper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    EditText name_et;
    EditText password_et;
    DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        Button login_bt=findViewById(R.id.loginbutton);
        login_bt.setOnClickListener(this);
        name_et=findViewById(R.id.Acount);
        password_et=findViewById(R.id.passwd);
        name_et.setOnFocusChangeListener(this);
        password_et.setOnFocusChangeListener(this);
        db=new DBHelper(this);
        db.InitialWithTestData(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginbutton:

                login_action();
                break;
        }
    }
    private void login_action(){
        String name= name_et.getText().toString();
        String password=password_et.getText().toString();
        if(!db.haveUser(name)){
            Toast.makeText(this, "该用户不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        if(db.loginWith(name,password)){
            Toast.makeText(this, "登录成功！", Toast.LENGTH_SHORT).show();
            //todo
            // 登录成功，跳转到Home界面
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class );
            startActivity(intent);

        }
        else {
            Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.Acount:
                if (hasFocus)
                    ((EditText)v).setHint("");
                else
                    ((EditText)v).setHint(R.string.account);
                break;
            case R.id.passwd:

                if (hasFocus)
                    ((EditText)v).setHint("");
                else
                    ((EditText)v).setHint(R.string.passwd);
                break;
        }
    }
}