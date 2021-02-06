package com.example.testdemo.testModel.viewpager;

import androidx.viewpager.widget.ViewPager;

import com.example.testdemo.R;
import com.yoy.v_Base.ui.BaseDefaultActivity;

public class ViewPagerActivity extends BaseDefaultActivity {

    @Override
    public int getLayoutID() {
        return R.layout.activity_viewpager;
    }

    @Override
    public boolean isFullScreenWindow() {
        return true;
    }

    @Override
    public void onInit() {
        ViewPager viewPager = findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        setActionBar("ViewPager测试",true);
    }
}