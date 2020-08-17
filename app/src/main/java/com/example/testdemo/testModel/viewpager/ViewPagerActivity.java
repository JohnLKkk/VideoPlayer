package com.example.testdemo.testModel.viewpager;

import android.os.Bundle;

import com.example.testdemo.R;
import com.example.testdemo.base.BaseDefaultActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

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