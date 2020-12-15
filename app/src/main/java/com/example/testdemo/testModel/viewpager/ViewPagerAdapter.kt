package com.example.testdemo.testModel.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.*

/**
 * Created by Void on 2020/5/18 13:18
 */
class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val items: MutableList<Fragment> = ArrayList()

    init {
        items.add(FragmentViewPager(1))
        items.add(FragmentViewPager(2))
        items.add(FragmentViewPager(3))
    }

    override fun getItem(position: Int): Fragment = items[position]

    override fun getCount(): Int = items.size
}