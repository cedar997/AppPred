package com.rom471.lab1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent=getIntent();
        String action=intent.getAction();
        String type=intent.getType();
        if(intent.ACTION_SEND.equals(action)&& type!=null){
            if("text/plain".equals(type)){
                intent.setClass(this,MainActivity.class);
                startActivity(intent);
            }
        }
        finish();
    }
}