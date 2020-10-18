package com.rom471.wjf;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rom471.lab1.R;
import com.rom471.userdb.DBHelper;

import org.w3c.dom.NamedNodeMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    EditText name_et;
    EditText password_et;
    DBHelper db;
    ImageView avatar_img;
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

                if (hasFocus) {
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
                    ((EditText) v).setHint("");
                }
                else
                    ((EditText)v).setHint(R.string.passwd);
                break;
        }
    }
}