package com.rom471.main;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import com.rom471.lab1.R;
import com.rom471.main.fragments.AllAppsFragment;
import com.rom471.main.fragments.FavoriteFragment;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener  {
    public static final String TAG="cedar";
    RadioGroup mRadioGroup;
    RadioButton rb1,rb2;

    String name; //登录的用户名
    private List<Fragment> fragments = new ArrayList<>();
    private Fragment fragment;
    private FragmentManager fm;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //绑定资源
        setContentView(R.layout.activity_home);
        mRadioGroup=findViewById(R.id.radioGroup1);
        rb1=findViewById(R.id.radio1);
        rb2=findViewById(R.id.radio2);
        //初始Fragments
        initFragments();
        //设置默认显示fragment
        setDefaultFragment();
        //设置监听器
        mRadioGroup.setOnCheckedChangeListener(this);
    }
    //返回登录界面传递的用户名信息
    public String getName(){
        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        return name;
    }
    //设置默认的fragment为收藏界面
    private void setDefaultFragment(){
        fm=getFragmentManager();
        transaction=fm.beginTransaction();
        fragment=fragments.get(1);
        transaction.replace(R.id.mFragment,fragment,"favorite");
        transaction.commit();
    }
   //初始化Fragments
    private void initFragments(){
        fragments.add(new AllAppsFragment());
        fragments.add(new FavoriteFragment());
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        transaction=fm.beginTransaction();
        switch (checkedId){
            case R.id.radio1:
                fragment=fragments.get(0);
                transaction.replace(R.id.mFragment,fragment,"all");
                break;
            case R.id.radio2:
                fragment=fragments.get(1);
                transaction.replace(R.id.mFragment,fragment,"favorite");
                break;
        }
        transaction.commit();
    }
}