package com.rom471.recorder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.rom471.recorder.fragments.HomeFragment;
import com.rom471.recorder.fragments.RecordFragment;
import com.rom471.recorder.fragments.SettingsFragment;
import com.rom471.utils.MyProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    public static final String TAG="cedar";
    RadioGroup mRadioGroup;
    RadioButton rb1,rb2,rb3;
    ImageView search_btn;
    EditText search_box;
    private List<Fragment> fragments = new ArrayList<>();
    private Fragment fragment;
    private FragmentManager fm;
    private FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        bindView();//绑定资源
        initFragments();
        mRadioGroup.setOnCheckedChangeListener(this);
        setDefaultFragment();

        initProperties();

    }
    private void setDefaultFragment(){
        fm=getFragmentManager();
        transaction=fm.beginTransaction();
        fragment=fragments.get(1);
        transaction.replace(R.id.mFragment,fragment);
        transaction.commit();
    }
    private void bindView(){
        mRadioGroup=findViewById(R.id.radioGroup1);
        rb1=findViewById(R.id.radio1);
        rb2=findViewById(R.id.radio2);
        rb3=findViewById(R.id.radio3);


    }
    //初始化参数
    private void initProperties(){
        if(MyProperties.getProperties(this).contains("dbname")) //如果存在则不初始
            return;
        Map<String, String> config_map = new HashMap<String, String>();
        config_map.put("dbname","app.db");
        config_map.put("db_index","0");
        MyProperties.setPropertiesMap(this,config_map);
    }
    private void initFragments(){
        fragments.add(new RecordFragment());
        fragments.add(new HomeFragment());

        fragments.add(new SettingsFragment());
    }
    public void print(String ... s){

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        transaction=fm.beginTransaction();
        switch (checkedId){
            case R.id.radio1:
                fragment=fragments.get(0);
                transaction.replace(R.id.mFragment,fragment);
                break;
            case R.id.radio2:
                fragment=fragments.get(1);
                transaction.replace(R.id.mFragment,fragment);
                break;
            case R.id.radio3:
                fragment=fragments.get(2);
                transaction.replace(R.id.mFragment,fragment);
                break;

        }
        transaction.commit();
    }


}