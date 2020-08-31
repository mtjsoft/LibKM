package cn.mtjsoft.libkotlinmvvm

import android.content.Context
import com.mtjsoft.www.kotlinmvputils.BaseApplication
import com.mtjsoft.www.kotlinmvputils.utils.KLog
import okhttp3.OkHttpClient
import rxhttp.wrapper.param.RxHttp
import java.util.concurrent.TimeUnit

class MyApp : BaseApplication() {

    private val modulesList = arrayListOf(
        "com.suntront.module_login.ModuleApp"
    )

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        // 安装tinker
//        Beta.installTinker()
    }

    override fun onCreate() {
        super.onCreate()
        KLog.init(true)
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        RxHttp.init(okHttpClient, BuildConfig.DEBUG)

        modulesApplicationInit(modulesList)
        // 设置升级检查周期为60s(默认检查周期为0s)，60s内SDK不重复向后台请求策略)
//          Beta.upgradeCheckPeriod = 60 * 1000
        // 升级SDK默认是开启热更新能力的，如果你不需要使用热更新，可以将这个接口设置为false。
//        Beta.enableHotfix = true
//        // wifi自动下载
//        Beta.autoDownloadOnWifi = true
//        // 设置启动延时 3 秒
//        Beta.initDelay = 3000
//        // 自动初始化开关
//        Beta.autoInit = true
//        // 自动检查更新开关
//        Beta.autoCheckUpgrade = true
//        // 设置是否显示弹窗中的apk信息
//        Beta.canShowApkInfo = true
//        // 设置自定义升级对话框UI布局
//        Beta.upgradeDialogLayoutId = R.layout.bugly_upgrade_dialog
//        // 设置自定义tip弹窗UI布局
//        Beta.tipsDialogLayoutId = R.layout.bugly_tips_dialog
//        // Bugly初始化
//        Bugly.init(applicationContext, "", BuildConfig.DEBUG)
    }
}