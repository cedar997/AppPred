package com.rom471.userdb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.rom471.lab1.R;

import java.util.Arrays;
import java.util.List;

public class LoginTestActivity extends AppCompatActivity {
    public static final String TAG="cedar";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DBHelper db=new DBHelper(this);
        db.InitialWithTestData(this);
        boolean a = db.loginWith("xxs", "cedar");
        boolean b = db.loginWith("xxs", "da");
        Log.d(TAG,"xxs="+a+" wkq="+b);
        db.haveUser("wkq");
    }

}