
package com.rom471.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.rom471.lab1.R;
import com.rom471.userdb.MyUtils;
import com.rom471.userdb.UsersDBHelper;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    Button resetPassword_btn;
    EditText name_et;
    EditText email_et;
    EditText newPassword_et;
    ImageView avatar_img;
    UsersDBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        //绑定组件
        resetPassword_btn=findViewById(R.id.register_button);
        name_et=findViewById(R.id.name_et);
        newPassword_et=findViewById(R.id.passwd_et);
        email_et=findViewById(R.id.email_et);
        avatar_img=findViewById(R.id.avatar_img);
        avatar_img.setOnClickListener(this);
        resetPassword_btn.setOnClickListener(this);
        db=new UsersDBHelper(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.register_button) {
            String name = name_et.getText().toString();
            String password = newPassword_et.getText().toString();
            String email = email_et.getText().toString();

            if (db.haveUser(name) && email.equals("")) {
                db.updatePasswordByName(name, password);
                MyUtils.toast(this, "密码已重置");
                // 密码重置成功，跳转到Login界面
                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
//                    intent.putExtra("name",name);
                startActivity(intent);
                this.finish();

            } else if (db.haveUser(name) && db.haveUser(email)) {
                db.updatePasswordByNameAndEmail(name, email, password);
                MyUtils.toast(this, "密码已重置");
                // 密码重置成功，跳转到Login界面
                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
//                    intent.putExtra("name",name);
                startActivity(intent);
                this.finish();

            } else {
                MyUtils.toast(this, "该用户不存在！");
                // 密码重置失败，跳转到Login界面
                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
//                    intent.putExtra("name",name);
                startActivity(intent);
                this.finish();
            }

            db.close();
            //this.finish();
        }
    }

}