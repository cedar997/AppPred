package com.rom471.main;
import androidx.appcompat.app.AppCompatActivity;
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
        //设置监听器
        avatar_img.setOnClickListener(this);
        resetPassword_btn.setOnClickListener(this);
        //初始化数据库操作类
        db=new UsersDBHelper(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.register_button) {
            String name = name_et.getText().toString();
            String password = newPassword_et.getText().toString();
            String email = email_et.getText().toString();
            if(!db.haveUser(name)){
                MyUtils.toast(this, "该用户不存在！");
                return;
            }
            //修改密码成功会产生变化
            int i = db.updatePasswordByNameAndEmail(name, email, password);
            if(i<=0){ //没有变化，代表邮箱不匹配
                MyUtils.toast(this, "邮箱信息不匹配");
                return;
            }else {
                // 密码重置成功
                MyUtils.toast(this, "密码重设成功");

            }


        }
    }

}