package com.rom471.ui;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.rom471.adapter.MyFragmentPagerAdapter;
import com.rom471.recorder.R;
import com.rom471.utils.MyProperties;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener , ViewPager.OnPageChangeListener {
    public static final String TAG="cedar";
    RadioGroup mRadioGroup;
    RadioButton rb1,rb2,rb3;
    ViewPager vp;
    private MyFragmentPagerAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mAdapter=new MyFragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        bindView();//绑定资源
        vp.setAdapter(mAdapter);
        vp.setCurrentItem(0);
        vp.addOnPageChangeListener(this);
        mRadioGroup.setOnCheckedChangeListener(this);
        initProperties();
    }

    private void bindView(){
        mRadioGroup=findViewById(R.id.radioGroup1);
        rb1=findViewById(R.id.radio1);
        rb2=findViewById(R.id.radio2);
        rb3=findViewById(R.id.radio3);
        vp=findViewById(R.id.vpager);


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



    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId){
            case R.id.radio1:
                vp.setCurrentItem(0);

                break;
            case R.id.radio2:
                vp.setCurrentItem(1);

                break;
            case R.id.radio3:
                vp.setCurrentItem(2);
                break;

        }

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if(state==2){
            switch (vp.getCurrentItem()){
                case 0:
                    rb1.setChecked(true);

                    break;
                case 1:
                    rb2.setChecked(true);

                    break;
                case 2:
                    rb3.setChecked(true);
                    break;
            }
        }
    }
}