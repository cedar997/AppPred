package com.rom471.main;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.rom471.lab1.R;
import com.rom471.userdb.DBHelper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    EditText name_et;//用户名输入框
    EditText password_et;//密码输入框
    DBHelper db; //数据库建立工具
    ImageView avatar_img; //头像显示图片
    Button login_bt;//登录按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        //绑定资源
        avatar_img = findViewById(R.id.pic);
        name_et = findViewById(R.id.Acount);
        password_et = findViewById(R.id.passwd);
        login_bt = findViewById(R.id.loginbutton);
        //设置登陆按钮监听
        login_bt.setOnClickListener(this);
        //设置文本框焦点变化监听
        name_et.setOnFocusChangeListener(this);
        password_et.setOnFocusChangeListener(this);
        //初始化数据库
        db = new DBHelper(this);
        db.InitialWithTestData();//使用测试数据初始化
    }
    //按键处理
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginbutton:
                login_action();
                break;
        }
    }
    //点击登录按钮执行的操作
    private void login_action() {
        String name = name_et.getText().toString();
        String password = password_et.getText().toString();
        if (!db.haveUser(name)) {
            toast("该用户不存在！");
            return;
        }
        if (db.loginWith(name, password)) {
            // 登录成功，跳转到Home界面
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        } else {
            toast("密码错误");
        }
    }
    //输入框焦点变化时，会使hint消失或出现；如果是用户名输入框，还会更新头像信息
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.Acount:
                if (hasFocus)
                    ((EditText) v).setHint("");
                else {
                    ((EditText) v).setHint(R.string.name);
                    changeAvatarImage();
                }
                break;
            case R.id.passwd:
                if (hasFocus) {
                    ((EditText) v).setHint("");
                } else
                    ((EditText) v).setHint(R.string.passwd);
                break;
        }
    }
    //从输入框获取用户名，查询数据库，改变头像
    private void changeAvatarImage() {
        String name = name_et.getText().toString();//获取输入的用户名
        Drawable avatar_drawable = db.getAvatarByName(name);
        if (avatar_drawable != null) //数据库里有头像
            avatar_img.setImageDrawable(avatar_drawable);
        else //数据库里不存在该用户，或该用户没有头像，设置默认头像
            avatar_img.setImageResource(R.drawable.head);
    }
    //在合理位置显示Toast
    private void toast(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 100);
        toast.show();
    }
}