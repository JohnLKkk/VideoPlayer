package com.example.testdemo.testModel.viewpager;

import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.example.testdemo.R;
import com.yoy.v_Base.ui.BaseDefaultActivity;

public class ViewPagerActivity extends BaseDefaultActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewPager viewPager = findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        setActionBar("ViewPager测试",true);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_viewpager;
    }

    @Override
    public boolean isFullScreenWindow() {
        return true;
    }
}