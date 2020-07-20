package com.example.testdemo.testModel.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.testdemo.R

/**
 * Created by Void on 2020/5/18 10:48
 *
 */
class FragmentViewPager(index: Int) : Fragment() {
    private var bgId = 0

    init {
        bgId = when (index) {
            1 -> R.drawable.ic_bg1
            2 -> -1
            3 -> R.drawable.ic_bg3
            else -> -1
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_adapter, null)
        val iv = view.findViewById<ImageView>(R.id.bg)
        if (bgId != -1) {
            iv.setImageResource(bgId)
        } else {
            iv.setImageResource(R.color.colorAccent)
        }
        return view
    }

}