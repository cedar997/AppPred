package com.rom471.lab1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    EditText et1;
    EditText et2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bt1).setOnClickListener(this);
        findViewById(R.id.bt2).setOnClickListener(this);
        et1=(EditText)findViewById(R.id.et1);
        et2=(EditText)findViewById(R.id.et2);

    }

    @Override
    protected void onResume() {
        receiveFromIntent(et1,et2);
        super.onResume();

    }

    private void receiveFromIntent(EditText et1, EditText et2){
        Intent intent=getIntent();
        String type=intent.getType();
        if(type!=null){
            if("text/plain".equals(type)){
                String data=intent.getStringExtra(intent.EXTRA_TEXT);
                et1.setText(data);
                et2.setText(encode(data));
            }
        }
    }

    private String encode(String src){
        byte[] bytes= new byte[0];
        try {
            bytes = src.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }
    private String decode(String code){
        try {
            return new String(Base64.decode(code,Base64.NO_WRAP),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bt1://加密
                String src=et1.getText().toString();
                et2.setText(encode(src));
                break;
            case R.id.bt2://解密
                String code=et2.getText().toString();

                et1.setText(decode(code));
                break;
        }
    }
}