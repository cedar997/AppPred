package com.rom471.adapter;


import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import com.rom471.ui.fragments.HomeFragment;
import com.rom471.ui.fragments.RcordFragment;
import com.rom471.ui.fragments.SettingsFragment;

import java.util.ArrayList;
import java.util.List;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int PAGER_COUNT = 3;
    private List<Fragment> fragments = new ArrayList<>();
    private void initFragments(){
        fragments.add(new RcordFragment());
        fragments.add(new HomeFragment());
        fragments.add(new SettingsFragment());
    }
    public MyFragmentPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        initFragments();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment=fragments.get(0);
                break;
            case 1:
                fragment=fragments.get(1);
                break;
            case 2:
                fragment=fragments.get(2);
                break;

        }
        return fragment;

    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }
}
