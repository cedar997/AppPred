package com.rom471.ui;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.rom471.adapter.MyFragmentPagerAdapter;

import com.rom471.recorder.R;
import com.rom471.utils.AppUtils;
import com.rom471.utils.MyProperties;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener , ViewPager.OnPageChangeListener {
    public static final String TAG="cedar";
    RadioGroup mRadioGroup;
    RadioButton rb1,rb2,rb3,rb4;
    ViewPager vp;


    private MyFragmentPagerAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mAdapter=new MyFragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        bindView();//绑定资源
        vp.setAdapter(mAdapter);
        vp.setCurrentItem(1);
        vp.addOnPageChangeListener(this);
        mRadioGroup.setOnCheckedChangeListener(this);
        initProperties();
        AppUtils.init(this);

//
//        RecordsViewModelFactory recordsViewModelFactory=new RecordsViewModelFactory();
//        recordsViewModel=new ViewModelProvider(this,recordsViewModelFactory).get(RecordsViewModel.class);


    }

    private void bindView(){
        mRadioGroup=findViewById(R.id.radioGroup1);
        rb1=findViewById(R.id.radio_find);
        rb2=findViewById(R.id.radio_sort);
        rb3=findViewById(R.id.radio_pred);
        rb4=findViewById(R.id.radio_set);
        vp=findViewById(R.id.vpager);


    }
    //初始化参数
    private void initProperties(){
        if(!MyProperties.have(this,"dbname")){
            MyProperties.set(this,"dbname",""+System.currentTimeMillis());
            MyProperties.set(this,"debug",false);
        }
    }



    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId){
            case R.id.radio_find:
                vp.setCurrentItem(0);

                break;
            case R.id.radio_sort:
                vp.setCurrentItem(1);

                break;
            case R.id.radio_pred:
                vp.setCurrentItem(2);
                break;
            case R.id.radio_set:
                vp.setCurrentItem(3);
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
                case 3:
                    rb4.setChecked(true);
                    break;
            }
        }
    }
}