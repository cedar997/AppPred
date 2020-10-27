package com.rom471.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.rom471.lab1.R;
import com.rom471.main.fragments.HomeFragment;
import com.rom471.main.fragments.SettingsFragment;
import com.rom471.main.fragments.UsersFragment;


import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener , View.OnClickListener {
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
        setContentView(R.layout.activity_home);
        bindView();//绑定资源
        initFragments();
        mRadioGroup.setOnCheckedChangeListener(this);
        setDefaultFragment();


    }
    private void setDefaultFragment(){
        fm=getFragmentManager();
        transaction=fm.beginTransaction();
        fragment=fragments.get(0);
        transaction.replace(R.id.mFragment,fragment);
        transaction.commit();
    }
    private void bindView(){
        mRadioGroup=findViewById(R.id.radioGroup1);
        rb1=findViewById(R.id.radio1);
        rb2=findViewById(R.id.radio2);
        rb3=findViewById(R.id.radio3);
        search_btn=findViewById(R.id.search_btn);
        search_box=findViewById(R.id.search_box);
        search_btn.setOnClickListener(this);
    }
    private void initFragments(){
        fragments.add(new UsersFragment());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_btn:
                search_box.setVisibility(View.VISIBLE);
                break;
        }
    }
}