package com.mtjsoft.www.kotlinmvputils

import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Process
import android.os.StrictMode
import android.text.TextUtils
import androidx.multidex.MultiDexApplication
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.GsonBuilder
import com.mtjsoft.www.kotlinmvputils.glide.GlideLoadUtil
import com.mtjsoft.www.kotlinmvputils.imp.IComponentApplication
import com.mtjsoft.www.kotlinmvputils.manager.AndBaseTopViewInfo
import com.mtjsoft.www.kotlinmvputils.rxhttp.gson.DoubleDefault0Adapter
import com.mtjsoft.www.kotlinmvputils.rxhttp.gson.IntegerDefault0Adapter
import com.mtjsoft.www.kotlinmvputils.rxhttp.gson.LongDefault0Adapter
import com.mtjsoft.www.kotlinmvputils.rxhttp.gson.MyTypeAdapterFactory
import com.mtjsoft.www.kotlinmvputils.utils.KLog
import com.tencent.mmkv.MMKV
import com.yuyh.library.imgsel.ISNav
import rxhttp.RxHttpPlugins
import rxhttp.wrapper.converter.GsonConverter
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.*


abstract class BaseApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        //配置统一标题栏样式
        AndBaseTopViewInfo.titleSize = 16
        AndBaseTopViewInfo.titleMode = AndBaseTopViewInfo.TitleMode.CENTER
        AndBaseTopViewInfo.titleTextColor = Color.WHITE
        AndBaseTopViewInfo.backgroundColor = R.color.main_base_color
        AndBaseTopViewInfo.backLeftDrawable = R.drawable.ic_arrow_white_24dp
        //统一处理7.0文件路径问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val svb = StrictMode.VmPolicy.Builder()
            StrictMode.setVmPolicy(svb.build())
        }
        // 获取当前进程名
        val processName = getProcessName(Process.myPid())
        if (processName == null || processName == packageName) {
//            if (BuildConfig.DEBUG) {
//                ARouter.openLog()
//                ARouter.openDebug()
//            }
//            KLog.init(BuildConfig.DEBUG)
            ARouter.init(this)
            MMKV.initialize(this)
            // 自定义图片加载器
            ISNav.getInstance().init { context, path, imageView ->
                GlideLoadUtil.setImage2Glide(path, R.drawable.ic_default_image, imageView)
            }
            // 自定义Gson解析，将null替换成空字符串
            val gson = GsonBuilder()
                .disableHtmlEscaping()
                .registerTypeAdapter(Integer::class.java, IntegerDefault0Adapter())
                .registerTypeAdapter(Double::class.java, DoubleDefault0Adapter())
                .registerTypeAdapter(Long::class.java, LongDefault0Adapter())
                .registerTypeAdapterFactory(MyTypeAdapterFactory())
                .create()
            RxHttpPlugins.setConverter(GsonConverter.create(gson))
        }
    }

    /**
     * 使用反射
     * 初始化Module类的APP
     */
    fun modulesApplicationInit(list: ArrayList<String>) {
        for (moduleImpl in list) {
            try {
                val clazz = Class.forName(moduleImpl)
                val obj = clazz.newInstance()
                if (obj is IComponentApplication) {
                    obj.init(this)
                }
            } catch (e: ClassNotFoundException) {
                e.printStackTrace();
            } catch (e: IllegalAccessException) {
                e.printStackTrace();
            } catch (e: InstantiationException) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    fun getProcessName(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName: String = reader.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim { it <= ' ' }
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                reader?.close()
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
        }
        return null
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        if (newConfig.fontScale != 1f) {
            resources
        }
        super.onConfigurationChanged(newConfig)
    }

    override fun getResources(): Resources? {
        val res: Resources = super.getResources()
        if (res.configuration.fontScale != 1f) {
            val newConfig = Configuration()
            newConfig.setToDefaults()
            res.updateConfiguration(newConfig, res.displayMetrics)
        }
        return res
    }
}