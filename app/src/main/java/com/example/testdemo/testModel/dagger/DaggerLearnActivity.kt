package com.example.testdemo.testModel.dagger

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.testdemo.R
import javax.inject.Inject

class DaggerLearnActivity : AppCompatActivity() {
    @JvmField
    @Inject
    var a: AFile? = null
    @JvmField
    @Inject
    var b: BFile? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dagger)
        DaggerMainComponent.create().inject(this)
    }

    fun onClickBtn(view: View?) {
        a!!.run();
//        b!!.bRun()
    }
}