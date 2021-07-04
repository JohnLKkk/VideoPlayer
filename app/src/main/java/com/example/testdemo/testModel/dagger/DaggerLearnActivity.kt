package com.example.testdemo.testModel.dagger

import androidx.appcompat.app.AppCompatActivity
import com.example.testdemo.testModel.dagger.AFile
import com.example.testdemo.testModel.dagger.BFile
import android.os.Bundle
import android.view.View
import com.example.testdemo.R
import com.example.testdemo.testModel.dagger.DaggerMainComponent
import kotlinx.android.synthetic.main.activity_dagger.*
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
        onBtn.setOnClickListener {
            println("onClickBtn------------------------------")
        }

        DaggerMainComponent.create().inject(this)
    }

    fun onClickBtn(view: View?) {
//        a!!.run();
//        b!!.bRun()
        println("onClickBtn------------------------------")
    }
}