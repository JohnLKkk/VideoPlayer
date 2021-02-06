package com.example.testdemo.testModel.startPhoneApp

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.view.View
import com.example.testdemo.R
import com.yoy.v_Base.ui.BaseDefaultActivity
import com.yoy.v_Base.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_start_phone_app.*

class StartPhoneAppActivity : BaseDefaultActivity() {
    private val appItems = HashMap<String, ApplicationInfo>()

    override fun getLayoutID(): Int = R.layout.activity_start_phone_app

    override fun onInit() {
        getApps()
    }

    override fun isFullScreenWindow(): Boolean = true

    fun onStartApp(view: View) {
        val requestName = appNameEt.text.toString()
        if (appItems.containsKey(requestName)) {
            val app = appItems[requestName] ?: return
            startActivity(packageManager.getLaunchIntentForPackage(app.packageName))
        } else {
            ToastUtils.showShort(applicationContext, "app不存在！")
        }
    }

    private fun getApps() {
        val apps: List<ApplicationInfo> = queryFilterAppInfo()
        val stringBuffer = StringBuffer("APP列表:\n")
        for (app in apps) {
            val name: String = app.loadLabel(packageManager).toString()
            stringBuffer.append(name + ":" + app.packageName + "\n")
            appItems[name] = app
        }
        outputText.text = stringBuffer
    }

    private fun queryFilterAppInfo(): List<ApplicationInfo> {
        val pm: PackageManager = this.packageManager
        // 查询所有已经安装的应用程序
        val appInfoList: List<ApplicationInfo> = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES) // GET_UNINSTALLED_PACKAGES代表已删除，但还有安装目录的

        // 通过getPackageManager()的queryIntentActivities方法遍历,得到所有能打开的app的packageName
        val resolveInfoList: List<ResolveInfo> = packageManager.queryIntentActivities(Intent()
                .apply {
                    // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
                    action = Intent.ACTION_MAIN
                    addCategory(Intent.CATEGORY_LAUNCHER)
                }, 0)
        val allowPackages: MutableSet<String> = HashSet()
        for (resolveInfo in resolveInfoList) {
            allowPackages.add(resolveInfo.activityInfo.packageName)
        }

        val applicationInfos: MutableList<ApplicationInfo> = ArrayList()
        for (app in appInfoList) {
//            if((app.flags & ApplicationInfo.FLAG_SYSTEM) <= 0)//通过flag排除系统应用，会将电话、短信也排除掉
//            {
//                applicationInfos.add(app);
//            }
//            if(app.uid > 10000){//通过uid排除系统应用，在一些手机上效果不好
//                applicationInfos.add(app);
//            }
            if (allowPackages.contains(app.packageName)) {
                applicationInfos.add(app)
            }
        }
        return applicationInfos
    }
}